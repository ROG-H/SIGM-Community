/* Copyright (C) 2011 [Gobierno de Espana]
 * This file is part of "Cliente @Firma".
 * "Cliente @Firma" is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation;
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * Date: 11/01/11
 * You may contact the copyright holder at: soporte.afirma5@mpt.es
 */

package es.gob.afirma.keystores.mozilla;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.security.auth.callback.PasswordCallback;

import es.gob.afirma.core.AOCancelledOperationException;
import es.gob.afirma.core.misc.Platform;
import es.gob.afirma.keystores.AOKeyStore;
import es.gob.afirma.keystores.AOKeyStoreManager;
import es.gob.afirma.keystores.AOKeyStoreManagerException;
import es.gob.afirma.keystores.AggregatedKeyStoreManager;
import es.gob.afirma.keystores.KeyStoreUtilities;
import es.gob.afirma.keystores.callbacks.UIPasswordCallback;

/** Representa a un <i>AOKeyStoreManager</i> para acceso a almacenes de claves de Firefox accedidos
 *  v&iacute;a NSS en el que se tratan de forma unificada los m&oacute;dulos internos y externos. */
public class MozillaUnifiedKeyStoreManager extends AggregatedKeyStoreManager {

    private static final String ONLY_PKCS11 = "es.gob.afirma.keystores.mozilla.LoadSscdOnly"; //$NON-NLS-1$
    protected static final String INCLUDE_NATIVE_DNIE_P11 = "es.gob.afirma.keystores.mozilla.IncludeNativeDniePkcs11"; //$NON-NLS-1$

	/** Crea un <i>AOKeyStoreManager</i> para acceso a almacenes de claves de Firefox. */
	public MozillaUnifiedKeyStoreManager() {
		setKeyStoreType(AOKeyStore.MOZ_UNI);
	}

	@SuppressWarnings("static-method")
	protected Map<String, String> getExternalStores() {
		return MozillaKeyStoreUtilities.getMozillaPKCS11Modules(
			// Si no es Linux o NO nos han incidado que incluyamos controlador nativo DNIe, lo excluimos
			!Platform.OS.LINUX.equals(Platform.OS.LINUX) || !(Boolean.getBoolean(INCLUDE_NATIVE_DNIE_P11) || Boolean.parseBoolean(System.getenv(INCLUDE_NATIVE_DNIE_P11))), // Excluir modulos nativos DNIe
			true  // Incluir los PKCS#11 que esten instalados en el sistema pero no en Mozilla
		);
	}

	protected AOKeyStoreManager getNssKeyStoreManager() {
		return new NssKeyStoreManager(getParentComponent(), false);
	}

	/** Inicializa la clase gestora de almacenes de claves.
	 * @throws AOKeyStoreManagerException
	 *         Si no puede inicializarse ning&uacute;n almac&eacute;n de
	 *         claves, ni el NSS interno, ni ning&uacute;n PKCS#11 externo
	 *         definido en SecMod
	 * @throws IOException Cuando hay errores de entrada / salida */
	@Override
	public final void init(final AOKeyStore type,
			               final InputStream store,
			               final PasswordCallback pssCallBack,
			               final Object[] params,
			               final boolean forceReset) throws AOKeyStoreManagerException,
			                                                IOException {

		final Object parentComponent = params != null && params.length > 0 ? params[0] : null;

		if (!(Boolean.getBoolean(ONLY_PKCS11) || Boolean.parseBoolean(System.getenv(ONLY_PKCS11)))) {
			// Primero anadimos el almacen principal NSS
			final AOKeyStoreManager ksm = getNssKeyStoreManager();
			try {
				ksm.init(type, store, pssCallBack, params, forceReset);
			}
			catch(final Exception e) {
				LOGGER.severe(
					"No se ha podido cargar NSS, se continuara con los almacenes externos: " + e //$NON-NLS-1$
				);
			}
			addKeyStoreManager(ksm);
		}

		// Vamos ahora con los almacenes externos, que se limpian antes de usarse quitando DNIe (porque se usa
		// el controlador Java) y anadiendo modulos conocidos si se encuentran en el sistema.
		final Map<String, String> externalStores = getExternalStores();

		if (externalStores.size() > 0) {
			final StringBuilder logStr = new StringBuilder(
				"Encontrados los siguientes modulos PKCS#11 externos instalados en Mozilla / Firefox: " //$NON-NLS-1$
			);
			for (final String key : externalStores.keySet()) {
				logStr.append("'"); //$NON-NLS-1$
				logStr.append(externalStores.get(key));
				logStr.append("' "); //$NON-NLS-1$
			}
			LOGGER.info(logStr.toString());
		}
		else {
			LOGGER.info("No se han encontrado modulos PKCS#11 externos instalados en Firefox"); //$NON-NLS-1$
		}

		for (final String descr : externalStores.keySet()) {
			final AOKeyStoreManager tmpKsm = new AOKeyStoreManager();
			try {
				internalInitStore(tmpKsm, descr, parentComponent, forceReset, externalStores.get(descr));
			}
			catch (final AOCancelledOperationException ex) {
				LOGGER.warning(
					"Se cancelo el acceso al almacen externo  '" + descr + "', se continuara con el siguiente: " + ex //$NON-NLS-1$ //$NON-NLS-2$
				);
				continue;
			}
			catch (final Exception ex) {
				// En ciertos sistemas Linux fallan las inicializaciones la primera vez por culpa de PC/SC, reintentamos
				if (Platform.OS.LINUX.equals(Platform.getOS())) {
					try {
						internalInitStore(tmpKsm, descr, parentComponent, forceReset, externalStores.get(descr));
					}
					catch (final AOCancelledOperationException exc) {
						LOGGER.warning(
							"Se cancelo el acceso al almacen externo  '" + descr + "', se continuara con el siguiente: " + exc //$NON-NLS-1$ //$NON-NLS-2$
						);
						continue;
					}
					catch(final Exception e) {
						LOGGER.severe("No se ha podido inicializar el PKCS#11 '" + descr + "' tras haberlo intentado dos veces: " + ex); //$NON-NLS-1$ //$NON-NLS-2$
						continue;
					}
				}
				else {
					LOGGER.severe("No se ha podido inicializar el PKCS#11 '" + descr + "': " + ex); //$NON-NLS-1$ //$NON-NLS-2$
					continue;
				}
			}
			addKeyStoreManager(tmpKsm);

			LOGGER.info("El almacen externo '" + descr + "' ha podido inicializarse, se anadiran sus entradas"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		KeyStoreUtilities.addPreferredKeyStoreManagers(this, parentComponent);

		if (lacksKeyStores()) {
			LOGGER.warning("No se ha podido inicializar ningun almacen, interno o externo, de Firefox"); //$NON-NLS-1$
		}

	}

	private static void internalInitStore(final AOKeyStoreManager tmpKsm,
			                              final String descr,
			                              final Object parentComponent,
			                              final boolean forceReset,
			                              final String libName) throws AOKeyStoreManagerException, IOException {
		tmpKsm.init(
			AOKeyStore.PKCS11,
			null,
			new UIPasswordCallback(
				FirefoxKeyStoreMessages.getString("MozillaUnifiedKeyStoreManager.1") + " " + MozillaKeyStoreUtilities.getMozModuleName(descr.toString()), //$NON-NLS-1$ //$NON-NLS-2$
				parentComponent
			),
			new String[] {
				libName, descr.toString()
			},
			forceReset
		);
	}

}
