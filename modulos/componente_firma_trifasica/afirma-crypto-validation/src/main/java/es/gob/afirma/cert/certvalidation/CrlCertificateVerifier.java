/* Copyright (C) 2011 [Gobierno de Espana]
 * This file is part of "Cliente @Firma".
 * "Cliente @Firma" is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation;
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * Date: 11/01/11
 * You may contact the copyright holder at: soporte.afirma5@mpt.es
 */

package es.gob.afirma.cert.certvalidation;

import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

/** Validador de certificados X.509v3 por verificaci&oacute;n de listas de revocaci&oacute;n
 * y de periodo de validez contra el reloj del sistema.
 * Clase cedida por <a href="http://www.yohago.com/">YoHago</a>.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s */
public final class CrlCertificateVerifier extends CertificateVerifier {

	private final Properties conf = new Properties();

	/** Construye un validador de certificados por CRL.
	 * @param cert Certificado inicialmente a validar. */
	public CrlCertificateVerifier(final X509Certificate cert) {
		this(null, cert);
	}

	/** Construye un validador de certificados por CRL.
	 * @param confFile Fichero de propiedades con las opciones de configuraci&oacute;n
	 * @param cert Certificado inicialmente a validar. */
	public CrlCertificateVerifier(final String confFile, final X509Certificate cert) {
		if (confFile != null) {
			try {
				this.conf.load(CrlCertificateVerifier.class.getResourceAsStream(confFile));
			}
			catch (final Exception e) {
				throw new IllegalArgumentException(
					"No se ha podido cargar la configuracion del servidor (" + confFile + ": " + e, e //$NON-NLS-1$ //$NON-NLS-2$
				);
			}
		}
		final String issuerCertFile = this.conf.getProperty("issuerCertFile"); //$NON-NLS-1$
		if (issuerCertFile != null) {
			try {
				setIssuerCert(
					(X509Certificate) CertificateFactory.getInstance(
						"X.509" //$NON-NLS-1$
					).generateCertificate(
						OcspCertificateVerifier.class.getResourceAsStream(issuerCertFile)
					)
				);
			}
			catch (final CertificateException e) {
				throw new IllegalArgumentException(
					"No se ha podido cargar el certificado raiz del emisor (" + issuerCertFile + "): " + e, e //$NON-NLS-1$ //$NON-NLS-2$
				);
			}
		}
		setSubjectCert(cert);
	}

	@Override
	protected ValidationResult verifyRevocation(final X509Certificate cert) {
		return CrlHelper.verifyCertificateCRLs(
			cert,
			this.getIssuerCert() != null ? this.getIssuerCert().getPublicKey() : null,
			null
		);
	}

}
