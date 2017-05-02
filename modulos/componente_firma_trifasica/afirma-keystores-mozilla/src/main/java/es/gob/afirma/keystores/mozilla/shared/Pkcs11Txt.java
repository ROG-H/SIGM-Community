/* Copyright (C) 2011 [Gobierno de Espana]
 * This file is part of "Cliente @Firma".
 * "Cliente @Firma" is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation;
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * Date: 11/01/11
 * You may contact the copyright holder at: soporte.afirma5@mpt.es
 */

package es.gob.afirma.keystores.mozilla.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.core.misc.BoundedBufferedReader;
import es.gob.afirma.keystores.mozilla.AOSecMod.ModuleName;

/** Analizador del fichero "pkcs11.txt" para la configuraci&oacute;n especial de NSS compartido.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
final class Pkcs11Txt {

	private static final String PKCS11TXT_FILENAME = "pkcs11.txt"; //$NON-NLS-1$

	private Pkcs11Txt() {
		// No instanciable
	}

	static List<ModuleName> getModules() throws IOException {
		final File f = new File(SharedNssUtil.getSharedUserProfileDirectory() + File.separator + PKCS11TXT_FILENAME);
		if (!f.isFile()) {
			return new ArrayList<ModuleName>(0);
		}
		if (!f.canRead()) {
			Logger.getLogger("es.gob.afirma").warning( //$NON-NLS-1$
				"No hay permisos de lectura para 'pkcs11.txt' en " + f.getAbsolutePath() //$NON-NLS-1$
			);
			return new ArrayList<ModuleName>(0);
		}
		final List<ModuleName> ret = new ArrayList<ModuleName>();
		final Reader fr = new FileReader(f);
		final BufferedReader br = new BoundedBufferedReader(
			fr,
			512, // Maximo 512 lineas
			4096 // Maximo 4KB por linea
		);
	    String line;
	    while ((line = br.readLine()) != null) {
	    	final String lib = AOUtil.getRDNvalueFromLdapName("library", line.replace(" ", ",")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    	if (lib != null && !lib.trim().isEmpty()) {
	    		ret.add(
    				new ModuleName(
						lib.trim(),
						lib.trim().replace(" ", "_").replace(".", "_").replace("-", "_") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					)
				);
	    	}
	    }
	    br.close();
	    fr.close();
	    return ret;
	}

}
