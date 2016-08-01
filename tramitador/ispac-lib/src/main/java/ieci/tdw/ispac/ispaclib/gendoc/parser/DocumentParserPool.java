package ieci.tdw.ispac.ispaclib.gendoc.parser;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.util.Semaphore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * Gestor que mantiene un conjunto de componentes de combinaci&oacute;n de documentos (DocumentParser) previamente
 * conectados para conseguir dos objetivos:
 *
 * <br/>
 * <ul>
 * <li>Mejorar la escalabilidad al disponer de m&aacute;s de una instancia de parser de combinaci&oacute;n</li>
 * <li>Mejorar el rendimiento debido a que el componente de combinaci&oacute;n ya se encuentra conectado</li>
 * </ul>
 *
 * <br/>
 * El gestor de servicios de combinaci&oacute;n es un objeto de instancia &uacute;nica en el framework y debido a ello
 * se implementa utilizando el patr&oacute;n  <i>singleton</i>;
 *
 * @version $Revision: 1.1 $ $Date: 2007/04/24 11:14:45 $
 */
public class DocumentParserPool
{
	private static Logger logger = Logger.getLogger(DocumentParserPool.class);

	private static DocumentParserPool mInstance = null;

	private HashMap mParserPool = null;
	private Semaphore mSemaphore;
	private long mTimeout = 30000;

	private DocumentParserPool()
	throws ISPACException
	{
		ISPACConfiguration config = ISPACConfiguration.getInstance();

		mParserPool = new HashMap();
		String connection = config.get( ISPACConfiguration.OPEN_OFFICE_CONNECT);
		DocumentParserPooled parserpooled = new DocumentParserPooled( connection);
		DocumentParser documentparser = parserpooled.getDocument();
		
		//[Manu Ticket #86] INICIO - ALSIGM3 Usar varias instancias de OpenOffice
		if(compruebaConn(connection)){
			mParserPool.put( documentparser, parserpooled);
		}
		//[Manu Ticket #86] FIN - ALSIGM3 Usar varias instancias de OpenOffice
		
		String parameter = config.get( ISPACConfiguration.OPEN_OFFICE_ADDITIONAL_INSTANCES);
		if (parameter != null)
		{
			int count = Integer.parseInt( parameter);
			for (int i = 0; i < count; i++)
			{
				connection = config.get( ISPACConfiguration.OPEN_OFFICE_CONNECT + "_" + i);
				parserpooled = new DocumentParserPooled( connection);
				documentparser = parserpooled.getDocument();
				
				//[Manu Ticket #86] INICIO - ALSIGM3 Usar varias instancias de OpenOffice
				if(compruebaConn(connection)){
					mParserPool.put( documentparser, parserpooled);
				}
				//[Manu Ticket #86] FIN - ALSIGM3 Usar varias instancias de OpenOffice				
			}
		}

		String timeout=config.get(ISPACConfiguration.OPEN_OFFICE_TIMEOUT);
		mTimeout=30000;
		if (timeout!=null && timeout.length()>0)
		{
			try
			{
			    mTimeout=Integer.parseInt(timeout);
			} catch (NumberFormatException e)
			{
			}
		}

		mSemaphore = new Semaphore( mParserPool.size());
	}
	
	/**
	 * [Manu Ticket #86] INICIO - ALSIGM3 Usar varias instancias de OpenOffice
	 * 
	 * Comprueba si el servidor de OpenOffice est� aceptando conexiones, para evitar que luego intente conectar con �l y falle.
	 * @param connection - Cadena de conexi�n con el servidor de OpenOffice 
	 * @return
	 */
	public boolean compruebaConn(String connection){
		boolean resultado = false;
		
		try {
			XComponentContext xcomponentcontext = Bootstrap.createInitialComponentContext(null);
			XMultiComponentFactory xLocalServiceManager = xcomponentcontext.getServiceManager();
	
			Object xUrlResolver = xLocalServiceManager.createInstanceWithContext(	"com.sun.star.bridge.UnoUrlResolver", xcomponentcontext);
			XUnoUrlResolver urlResolver =
					(XUnoUrlResolver) UnoRuntime.queryInterface( XUnoUrlResolver.class, xUrlResolver);
			urlResolver.resolve(connection);
			resultado = true;
		}catch (java.lang.Exception e){
			logger.warn("Error al establecer la conexi�n con OpenOffice: " + connection + ". " + e.getMessage(), e);
			resultado = false;
		}
		return resultado;
	}
	//[Manu Ticket #86] FIN - ALSIGM3 Usar varias instancias de OpenOffice
	

	/**
	 * Devuelve la &uacute;nica instancia posible del gestor de parsers.
	 *
	 * @return El gestor de parsers de combinaci&oacute;n de documentos
	 * @throws ISPACException
	 */
	public static synchronized DocumentParserPool getInstance()
	throws ISPACException
	{
		//[Manu Ticket #86] INICIO - ALSIGM3 Usar varias instancias de OpenOffice 
		//(Se elimina para que siempre recupere las conexiones para ver si se ha ca�do alg�n servidor de OO)
		//if (mInstance == null)
			mInstance = new DocumentParserPool();
		//[Manu Ticket #86] FIN - ALSIGM3 Usar varias instancias de OpenOffice


		return mInstance;
	}


	/**
	 * Entrega un combinador de documentos preparado para su uso, es decir inicializado y
	 * conectado con el servicio de combinaci&oacute;n OpenOffice.
	 * Una vez utilizado es imprescindible devolverlo a este pool llamando
	 * al m&eacute;todo {@link #release(DocumentParser) release}.
	 *
	 * @return El componente combinador de documentos
	 * @throws ISPACException  El m&eacute;todo lanza una notificaci&oacute;n si se produce un timeout
	 */
	public DocumentParser get() throws ISPACException
	{
		if (!mSemaphore.acquire( mTimeout))
		    throw new ISPACInfo( "El servicio de generaci�n de documentos est� ocupado. Reintente la operaci�n m�s tarde.");

		synchronized(mParserPool)
		{
			Iterator iterator = mParserPool.entrySet().iterator();

		    while (iterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) iterator.next();
				DocumentParserPooled source = (DocumentParserPooled) entry.getValue();
				if (!source.isBusy())
				{
					source.setBusy( true);
					return source.getDocument();
				}
            }
		}
		//Por construcci�n s�lo llegar� aqu� si el pool de combinadores se corrompiera.
		//Nunca deber�a haber m�s lectores (hebras que deja pasar el sem�foro) que
		//combinadores DocumentParser en el pool
		mSemaphore.release();
		throw new ISPACException("Error en el pool de servicio de generaci�n de documentos. "+
		        "No se puede realizar la operaci�n. Reinicie el pool.");
	}

	/**
	 * Libera el combinador de documentos utilizado quedando desde este momento a disposici&oacute;n de otros clientes de
	 * este pool.
	 *
	 * @param parser  El combinador de documentos a liberar.
	 */
	public void release(DocumentParser parser)
	{
		if (parser == null)
		    return;

		synchronized(mParserPool)
		{
			if (mParserPool.containsKey( parser))
			{
				DocumentParserPooled source = (DocumentParserPooled) mParserPool.get( parser);
				source.setBusy( false);
			}
		}
		mSemaphore.release();
	}

	private class DocumentParserPooled
	{
    	boolean busy;
    	DocumentParser document;

    	public DocumentParserPooled( String connection)
    	throws ISPACException
    	{
    		busy = false;
    		document = new DocumentParser( connection);
    	}
    	public void setBusy( boolean busy)
    	{
    		this.busy = busy;
    	}
    	public boolean isBusy()
    	{
    		return this.busy;
    	}
    	public DocumentParser getDocument()
    	{
    		return document;
    	}
    }
}
