package es.sigem.dipcoruna.desktop.editlauncher.service.apps.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.editlauncher.service.apps.GeneralAppService;
import es.sigem.dipcoruna.framework.service.util.windows.WinRegistryUtil;

public abstract class AbstractGeneralAppServiceImpl implements GeneralAppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGeneralAppServiceImpl.class);
    
    
    @Override
    public String getPathAplicacionAsociadaConArchivosDeExtension(String extension) {
        if (!StringUtils.hasText(extension)) {
            LOGGER.error("Extensi�n no puede ser vac�a");
            throw new IllegalArgumentException("La extensi�n del archivo no puede ser vac�a");
        }
        return WinRegistryUtil.getAssociatedProgram("." + extension);
    }
    

    @Override
    public boolean existeLaAplicacion(String appPath) {
        return StringUtils.hasText(appPath) && new File(appPath).exists();
    }
    

}
