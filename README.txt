

            /\                                                        /\ 
            ||                                                        ||
            ||                                                        ||
            ||                                                        ||
       _.-~" "~-._                                                _.-~" "~-._
      ^           ^                      __                      ^           ^
     '             '                    (  )                    '             '
     Y             Y                 _.~-  -~._                 Y             Y
    ==             ==            __-^          ^-__            ==             ==
     |             |            |                  |            |             |
     |             |____________|                  |____________|             |
     |                                                                        |
     |                 ______________________________________                 |
     |       _________|                                      |_________       |
     |       \        |         DIPUTACI�N PROVINCIAL        |        /       |
     |        \       |            DE CIUDAD REAL            |       /        |
     |        /       |______________________________________|       \        |
     |       /____________)                              (____________\       |
     |                                                                        |
     |________________________________________________________________________|



- [Dipucr-Manu 28/06/2016] 
     Instrucciones para montar el c�digo de AL-SIGM, compilarlo y generar el despliegue 
completo de la aplicaci�n para poder ser arrancado en un servidor de aplicaciones.

===================
      �NDICE 

1.- Pre-requisitos.
2.- Compilaci�n del c�digo.
3.- Despliegue de la aplicaci�n.
4.- Licencia.

===================


1.- Pre-requisitos:
    1.1 Tener instalado el JDK que se indica en la documentaci�n.
    1.2 Tener instalado Maven 2 como se indica en la documentaci�n.
    1.3 Deben estar configuradas las variables del sistema:

        - JAVA_HOME con la ruta del '<<JDK>>'. 
            (Por ejemplo C:\Program Files (x86)\Java\jdk1.6.0_18)
        - M2_HOME con la ruta de instalaci�n del apache de maven. 
            (Por ejemplo C:\apaches\apache-maven-3.2.2)
        - PATH con las rutas %M2_HOME%;%M2_HOME%\bin;%JAVA_HOME%;%JAVA_HOME%\bin;
        - MAVEN_OPTS con el valor -Xmx1024m -XX:MaxPermSize=256m

    1.4 Modificar el archivo settings.xml de la carpeta .m2 del perfil del usuario 
(por ejemplo C:\Users\Manu\.m2) o del apache maven y a�adir un perfil con las rutas al JDK
que se utilizar� para compilar (Por ejemplo C:\Program Files (x86)\Java\jdk1.6.0_18):

     ...
    <profiles>
        ...
        <profile>
            <id>development</id>
            <properties>
                <JAVA_1_4_HOME>'<<JDK>>'</JAVA_1_4_HOME>
                <JAVA_1_5_HOME>'<<JDK>>'</JAVA_1_5_HOME>
            </properties>            
        </profile>
    </profiles>
        ...
    <activeProfiles>
        ...
        <activeProfile>development</activeProfile>
    </activeProfiles>
    ...

    1.5 Se comprueba que todo est� bien configurado abriendo un terminal nuevo y ejecutando 
los comandos:

     - javac (para comprobar la instalaci�n de jdc)
     - mvn (para comprobar la instalaci�n de mvn)

     En ambos caso debe dar un mensaje distinto a '''javac o mvn (seg�n el caso)" no se reconoce 
como un comando interno o externo, programa o archivo por lotes ejecutable.

2.- Compilaci�n del c�digo:
    Una vez descargado se realiza la primera compilaci�n necesaria para que se generen los 
artefactos necesarios tanto para desplegar directamente la aplicaci�n como para que compile 
el c�digo y se puedan realizar desarrollos.
    En la primera compilaci�n hay que ejecutar los siguientes comandos en el orden indicado desde 
la ruta donde se hay descagado el c�digo (por ejemplo C:\ALSIGM\alsigm):

    mvn clean install -Dmaven.test.skip=true -Dinit
    mvn clean install -Dmaven.test.skip=true -Djars
    mvn clean install -Dmaven.test.skip=true
    mvn clean install -Dmaven.test.skip=true -Dwars
    mvn clean install -Dmaven.test.skip=true -Dears -P generate-distri

3.- Despliegue de la aplicaci�n:
    Si todo ha finalizado correctamente se habr�n generado los WARs de los distintos m�dulos 
de la aplicaci�n en la carpeta ./alsigm/sigem/SIGEM_DIST que se podr�n desplegar en un servidor 
de aplicaciones (previamente configurado como se indica en la documentaci�n).


4.- Licencia

    La siguiente �Licencia P�blica de la Uni�n Europea� (�European Union Public Licence EUPL�) 
se ha elaborado en el marco de IDABC, programa de la Comunidad Europea cuyo objetivo es promover 
la prestaci�n interoperable de servicios de administraci�n electr�nica europea a las 
administraciones p�blicas, las empresas y los ciudadanos. IDABC prolonga y profundiza el anterior
programa IDA (�Intercambio de Datos entre Administraciones�).

EUPL 1.1 (https://joinup.ec.europa.eu/system/files/ES/EUPL%20v.1.1%20-%20Licencia.pdf)