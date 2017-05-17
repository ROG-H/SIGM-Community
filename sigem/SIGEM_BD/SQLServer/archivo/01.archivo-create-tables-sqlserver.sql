BEGIN TRANSACTION;

CREATE TABLE dbo.AANAUDITACCION (
  MODULO	INT  NOT NULL,
  ACCION  	INT  NOT NULL,
  NIVEL		INT  NOT NULL
);

CREATE TABLE dbo.AANAUDITUSR (
  TIPOAUDITADO	VARCHAR(32) NOT NULL,
  IDAUDITADO    VARCHAR(32) NOT NULL,
  NIVEL         INT NOT NULL
);

CREATE TABLE dbo.AADATOSTRAZA (
  ID          VARCHAR(32) NOT NULL,
  IDTRAZA     VARCHAR(32) NOT NULL,
  CONTENIDO   TEXT,
  TIPOOBJETO  INT,
  IDOBJETO    VARCHAR(32)
);

CREATE TABLE dbo.AATRAZA (
  ID         VARCHAR(32)	NOT NULL,
  MODULO     INT        	NOT NULL,
  ACCION     INT			NOT NULL,
  TIMESTAMP  DATETIME		NOT NULL,
  DIRIP      VARCHAR(32)	NOT NULL,
  IDUSUARIO  VARCHAR(32),
  CODERROR   INT
);

CREATE TABLE dbo.AASESION (
  IDUSUARIO  VARCHAR(32)	NOT NULL,
  KEEPALIVE  DATETIME		NOT NULL,
  ID         VARCHAR(64)	NOT NULL
);

CREATE TABLE dbo.ADAREA (
  ID           VARCHAR(32)	NOT NULL,
  NOMBRE       VARCHAR(128) NOT NULL,
  TIPONORMA    INT       	DEFAULT 0 NOT NULL,
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ADCAMPODATO (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64)  NOT NULL,
  TIPO         INT       	NOT NULL,
  TIPONORMA    INT       	NOT NULL,
  IDAREA       VARCHAR(32),
  IDTBLPADRE   VARCHAR(32),
  POSENTBL     INT,
  ETIQUETAXML  VARCHAR(128)  NOT NULL,
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ADCAMPOTBL (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64)  NOT NULL,
  TIPONORMA    INT       NOT NULL,
  IDAREA       VARCHAR(32),
  ETIQUETAXML  VARCHAR(128)  NOT NULL,
  ETIQXMLFILA  VARCHAR(128)  NOT NULL,
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ADCTLGLISTAD (
  ID                 VARCHAR(32)  	NOT NULL,
  NOMBRE             VARCHAR(64)  	NOT NULL,
  TIPO               INT       		NOT NULL,
  TIPODESCRIPTOR     INT 			NOT NULL,
  IDFICHADESCRPREF   VARCHAR(32),
  IDFICHACLFDOCPREF  VARCHAR(32),
  IDREPECMPREF         VARCHAR(32),
  DESCRIPCION        VARCHAR(254)
);

CREATE TABLE dbo.ADCTLGTBLVLD (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64)  NOT NULL,
  DESCRIPCION  VARCHAR(254),
  USOINTERNO   CHAR (1)
);

CREATE TABLE dbo.ADDESCRIPTOR (
  ID              VARCHAR(32)  	NOT NULL,
  NOMBRE          VARCHAR(512)  NOT NULL,
  IDLISTA         VARCHAR(32)  	NOT NULL,
  TIPO            INT       	NOT NULL,
  ESTADO          INT       	NOT NULL,
  IDSISTEXT       VARCHAR(64),
  IDDESCRSISTEXT  VARCHAR(32),
  TIMESTAMP       DATETIME     	NOT NULL,
  IDFICHADESCR    VARCHAR(32),
  TIENEDESCR      CHAR (1),
  EDITCLFDOCS     CHAR (1),
  IDREPECM          VARCHAR(32),
  NIVELACCESO     SMALLINT NOT NULL,
  IDLCA           VARCHAR(32),
  IDDOCUMENTAL    BIGINT IDENTITY (1, 1) NOT NULL
);

CREATE TABLE dbo.ADFICHA (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64)  NOT NULL,
  DEFINICION   TEXT			NOT NULL,
  TIPONORMA    INT       	NOT NULL,
  TIPONIVEL    INT       	NOT NULL,
  SUBTIPONIVEL INT       	DEFAULT 0 NOT NULL,
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ADFMTFICHA (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(128)  NOT NULL,
  DEFINICION   TEXT         NOT NULL,
  IDFICHA      VARCHAR(32)  NOT NULL,
  TIPO         SMALLINT    NOT NULL,
  NIVELACCESO  SMALLINT    NOT NULL,
  IDLCA        VARCHAR(32),
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ADFMTPREF (
  IDFICHA    VARCHAR(32)  NOT NULL,
  IDUSUARIO  VARCHAR(32)  NOT NULL,
  TIPOFMT    SMALLINT    NOT NULL,
  IDFMT      VARCHAR(32)  NOT NULL
);

CREATE TABLE dbo.ADOCCLASIFCF (
  ID            VARCHAR(32)  NOT NULL,
  NOMBRE        VARCHAR(128)  NOT NULL,
  IDCLFPADRE    VARCHAR(32),
  IDELEMENTOCF  VARCHAR(32)  NOT NULL,
  MARCAS        INT       NOT NULL,
  ESTADO        INT       NOT NULL,
  DESCRIPCION   VARCHAR(512)
);

CREATE TABLE dbo.ADOCCLASIFDESCR (
  ID            VARCHAR(32)  NOT NULL,
  NOMBRE        VARCHAR(128)  NOT NULL,
  IDCLFPADRE    VARCHAR(32),
  IDDESCR       VARCHAR(32)  NOT NULL,
  MARCAS        INT       NOT NULL,
  ESTADO        INT       NOT NULL,
  DESCRIPCION   VARCHAR(254)
);

CREATE TABLE dbo.ADOCDOCUMENTOCF (
  ID            VARCHAR(32)  NOT NULL,
  NOMBRE        VARCHAR(128)  NOT NULL,
  IDCLFPADRE    VARCHAR(32),
  IDELEMENTOCF  VARCHAR(32)  NOT NULL,
  TAMANOFICH    FLOAT          NOT NULL,
  NOMBREORGFICH VARCHAR(254) NOT NULL,
  EXTFICH       VARCHAR(16)  NOT NULL,
  IDEXTDEPOSITO VARCHAR(32),
  IDFICH        VARCHAR(128)  NOT NULL,
  ESTADO        INT       NOT NULL,
  DESCRIPCION   VARCHAR(254),
  IDREPECM      VARCHAR(32)
);

CREATE TABLE dbo.ADOCDOCUMENTODESCR (
  ID            VARCHAR(32)  NOT NULL,
  NOMBRE        VARCHAR(128)  NOT NULL,
  IDCLFPADRE    VARCHAR(32),
  IDDESCR       VARCHAR(32)  NOT NULL,
  TAMANOFICH    FLOAT          NOT NULL,
  NOMBREORGFICH VARCHAR(254) NOT NULL,
  EXTFICH       VARCHAR(16)  NOT NULL,
  IDFICH        VARCHAR(128)  NOT NULL,
  ESTADO        INT       NOT NULL,
  DESCRIPCION   VARCHAR(254),
  IDREPECM      VARCHAR(32)
);

CREATE TABLE dbo.ADOCFICHACLF (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64)  NOT NULL,
  DEFINICION   TEXT          NOT NULL,
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ADOCTCAPTURA (
  ID           	VARCHAR(32)  NOT NULL,
  TIPOOBJ      	INT        NOT NULL,
  IDOBJ        	VARCHAR(32)  NOT NULL,
  IDUSRCAPTURA 	VARCHAR(32)  NOT NULL,
  CODREFOBJ    	VARCHAR(1024),
  TITULOOBJ    	VARCHAR(1024) NOT NULL,
  ESTADO       	INT        NOT NULL,
  FECHAESTADO  	DATETIME           NOT NULL,
  OBSERVACIONES VARCHAR(254),
  MOTIVOERROR  	VARCHAR(254)
);

CREATE TABLE dbo.ADPCDOCUMENTOVIT (
  ID           	VARCHAR(32)  NOT NULL,
  IDBDTERCEROS 	VARCHAR(64)  NOT NULL,
  NUMIDENTIDAD 	VARCHAR(32),
  IDENTIDAD    	VARCHAR(254),
  IDTIPODOCVIT 	VARCHAR(32)  NOT NULL,
  FECHACAD     	DATETIME,
  ESTADODOCVIT 	INT        NOT NULL,
  FECHACR      	DATETIME           NOT NULL,
  IDUSUARIOCR  	VARCHAR(32)  NOT NULL,
  FECHAVIG     	DATETIME,
  IDUSUARIOVIG 	VARCHAR(32),
  NUMACCESOS   	INT        NOT NULL,
  FECHAULTACCESO DATETIME,
  TAMANOFICH   	FLOAT          NOT NULL,
  NOMBREORGFICH VARCHAR(254) NOT NULL,
  EXTFICH      	VARCHAR(16)  NOT NULL,
  IDFICH       	VARCHAR(128)  NOT NULL,
  OBSERVACIONES	VARCHAR(254),
  IDREPECM		VARCHAR(32)
);

CREATE TABLE dbo.ADPCUSODOCVIT (
  IDDOCVIT     VARCHAR(32)  NOT NULL,
  IDEXP        VARCHAR(254)  NOT NULL,
  IDSIST       VARCHAR(254)  NOT NULL,
  FECHAUSO     DATETIME       NOT NULL,
  USUARIO      VARCHAR(254)  NOT NULL
);

CREATE TABLE dbo.ADPCTIPODOCVIT (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(254)  NOT NULL,
  DESCRIPCION  VARCHAR(512)
);

CREATE TABLE dbo.ADPCTIPODOCPROC (
  IDTIPODOC    VARCHAR(32)  NOT NULL,
  IDPROC       VARCHAR(254)  NOT NULL
);

CREATE TABLE dbo.ADTEXTTBLVLD (
  ID        VARCHAR(32)  NOT NULL,
  VALOR     VARCHAR(254)  NOT NULL,
  IDTBLVLD  VARCHAR(32)  NOT NULL
);

CREATE TABLE dbo.ADUSOOBJETO (
  IDOBJ           VARCHAR(32)  NOT NULL,
  TIPOOBJ         INT       NOT NULL,
  IDOBJUSUARIO    VARCHAR(32)  NOT NULL,
  TIPOOBJUSUARIO  INT       NOT NULL
);

CREATE TABLE dbo.ADVCFECHACF (
  IDELEMENTOCF  VARCHAR(32)  NOT NULL,
  IDCAMPO       VARCHAR(32)  NOT NULL,
  VALOR         VARCHAR(64),
  FECHAINI      DATETIME,
  FECHAFIN      DATETIME,
  CALIFICADOR   VARCHAR(32),
  ORDEN         INT       NOT NULL,
  TIMESTAMP     DATETIME          NOT NULL,
  FORMATO       VARCHAR(16),
  SEP           CHAR (1)
);

CREATE TABLE dbo.ADVCFECHADESCR (
  IDDESCR      VARCHAR(32)  NOT NULL,
  IDCAMPO      VARCHAR(32)  NOT NULL,
  VALOR        VARCHAR(64),
  FECHAINI     DATETIME,
  FECHAFIN     DATETIME,
  FORMATO      VARCHAR(16),
  SEP          CHAR (1),
  CALIFICADOR  VARCHAR(32),
  ORDEN        INT       	NOT NULL,
  TIMESTAMP    DATETIME 	NOT NULL
);

CREATE TABLE dbo.ADVCNUMCF (
  IDELEMENTOCF  VARCHAR(32)  NOT NULL,
  IDCAMPO       VARCHAR(32)  NOT NULL,
  VALOR         FLOAT NOT NULL,
  ORDEN         INT       NOT NULL,
  TIMESTAMP     DATETIME      NOT NULL,
  TIPOMEDIDA    INT,
  UNIDADMEDIDA  VARCHAR(16)
);

CREATE TABLE dbo.ADVCNUMDESCR (
  IDDESCR       VARCHAR(32)  NOT NULL,
  IDCAMPO       VARCHAR(32)  NOT NULL,
  VALOR         FLOAT NOT NULL,
  ORDEN         INT       NOT NULL,
  TIMESTAMP     DATETIME      NOT NULL,
  TIPOMEDIDA    INT,
  UNIDADMEDIDA  VARCHAR(16)
);

CREATE TABLE dbo.ADVCREFCF (
  IDELEMENTOCF  VARCHAR(32) NOT NULL,
  IDCAMPO       VARCHAR(32) NOT NULL,
  TIPOOBJREF    INT       	NOT NULL,
  IDOBJREF      VARCHAR(32) NOT NULL,
  ORDEN         INT       	NOT NULL,
  TIMESTAMP     DATETIME  	NOT NULL
);

CREATE TABLE dbo.ADVCREFDESCR (
  IDDESCR     VARCHAR(32)  	NOT NULL,
  IDCAMPO     VARCHAR(32)  	NOT NULL,
  TIPOOBJREF  INT       	NOT NULL,
  IDOBJREF    VARCHAR(32)  	NOT NULL,
  ORDEN       INT       	NOT NULL,
  TIMESTAMP   DATETIME      NOT NULL
);

CREATE TABLE dbo.ADVCTEXTCF (
  IDELEMENTOCF  VARCHAR(32)  NOT NULL,
  IDCAMPO       VARCHAR(32)  NOT NULL,
  VALOR         VARCHAR(254)  NOT NULL,
  ORDEN         INT       NOT NULL,
  TIMESTAMP     DATETIME          NOT NULL,
  IDDOCUMENTAL  BIGINT IDENTITY (1, 1) NOT NULL
);

CREATE TABLE dbo.ADVCTEXTDESCR (
  IDDESCR    	VARCHAR(32)  NOT NULL,
  IDCAMPO    	VARCHAR(32)  NOT NULL,
  VALOR      	VARCHAR(254)  NOT NULL,
  ORDEN      	INT       NOT NULL,
  TIMESTAMP  	DATETIME          NOT NULL,
  IDDOCUMENTAL  BIGINT IDENTITY (1, 1) NOT NULL
);

CREATE TABLE dbo.ADVCTEXTLCF (
  IDELEMENTOCF  VARCHAR(32)  NOT NULL,
  IDCAMPO       VARCHAR(32)  NOT NULL,
  VALOR         TEXT          NOT NULL,
  ORDEN         INT       NOT NULL,
  TIMESTAMP     DATETIME          NOT NULL,
  IDDOCUMENTAL  BIGINT IDENTITY (1, 1) NOT NULL
);

CREATE TABLE dbo.ADVCTEXTLDESCR (
  IDDESCR    	VARCHAR(32)  NOT NULL,
  IDCAMPO    	VARCHAR(32)  NOT NULL,
  VALOR      	TEXT          NOT NULL,
  ORDEN      	INT       NOT NULL,
  TIMESTAMP  	DATETIME          NOT NULL,
  IDDOCUMENTAL  BIGINT IDENTITY (1, 1) NOT NULL
);

CREATE TABLE dbo.AGARCHIVO (
  ID      			VARCHAR(32) NOT NULL,
  CODIGO  			VARCHAR(32) NOT NULL,
  NOMBRE  			VARCHAR(128) NOT NULL,
  IDNIVEL  			VARCHAR(32) NOT NULL,
  IDRECEPTORDEFECTO VARCHAR(32),
  NOMBRECORTO 		VARCHAR(25) NOT NULL,
  TIPOSIGNATURACION INT DEFAULT 1 NOT NULL
);

CREATE TABLE dbo.AGNIVELARCHIVO(
   ID 			VARCHAR(32) NOT NULL,
   NOMBRE 		VARCHAR(64) NOT NULL,
   DESCRIPCION 	VARCHAR(254),
   ORDEN 		SMALLINT NOT NULL
);

CREATE TABLE dbo.AGFORMATO (
  ID          VARCHAR(32)  NOT NULL,
  NOMBRE      VARCHAR(64)  NOT NULL,
  TIPO        SMALLINT    NOT NULL,
  ESMULTIDOC  SMALLINT    NOT NULL,
  REGULAR     SMALLINT    NOT NULL,
  LONGITUD    FLOAT,
  XINFO       VARCHAR(1024),
  VIGENTE     SMALLINT    NOT NULL
);

CREATE TABLE dbo.AGOBJBLOQUEO (
  IDOBJ      VARCHAR(32)  NOT NULL,
  TIPOOBJ    SMALLINT        NOT NULL,
  MODULO     SMALLINT        NOT NULL,
  TS         DATETIME          NOT NULL,
  IDUSUARIO  VARCHAR(32)  NOT NULL
);

CREATE TABLE dbo.ASCAGRUPO (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(254) NOT NULL,
  IDARCHIVO    VARCHAR(32),
  DESCRIPCION  VARCHAR(254),
  INFO VARCHAR(1024) DEFAULT ''
);

CREATE TABLE dbo.ASCALISTCA (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(254) NOT NULL,
  TIPO         SMALLINT NOT NULL,
  DESCRIPCION  VARCHAR(512)
);

CREATE TABLE dbo.ASCAORGANO (
  ID                 VARCHAR(32)  NOT NULL,
  CODIGO             VARCHAR(64) NOT NULL,
  NOMBRE             VARCHAR(254) NOT NULL,
  NOMBRELARGO        VARCHAR(1024),
  IDARCHIVORECEPTOR  VARCHAR(32) NOT NULL,
  SISTEXTGESTOR      VARCHAR(32) NOT NULL,
  IDORGSEXTGESTOR    VARCHAR(64) NOT NULL,
  VIGENTE            CHAR (1) NOT NULL,
  DESCRIPCION        VARCHAR(254)
);

CREATE TABLE dbo.ASCAPERMGENROL (
  IDROL       VARCHAR(32) NOT NULL,
  TIPOMODULO  SMALLINT NOT NULL,
  PERM        INT NOT NULL
);

CREATE TABLE dbo.ASCAPERMLISTCA (
  IDLISTCA  VARCHAR(32) NOT NULL,
  TIPODEST  SMALLINT NOT NULL,
  IDDEST    VARCHAR(64) NOT NULL,
  PERM      INT NOT NULL
);

CREATE TABLE dbo.ASCAROL (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64) NOT NULL,
  TIPOMODULO   SMALLINT,
  DESCRIPCION  VARCHAR(254)
);

CREATE TABLE dbo.ASCAROLUSR (
  IDROL       VARCHAR(32)  NOT NULL,
  TIPOMODULO  SMALLINT  NOT NULL,
  IDUSUARIO   VARCHAR(32)  NOT NULL
);

CREATE TABLE dbo.ASCAUSRGRP (
  IDUSUARIO  VARCHAR(32) NOT NULL,
  IDGRUPO    VARCHAR(32) NOT NULL
);

CREATE TABLE dbo.ASCAUSRORG (
  IDUSUARIO  VARCHAR(32) NOT NULL,
  IDORGANO   VARCHAR(32) NOT NULL,
  FECHAINI   DATETIME,
  FECHAFIN   DATETIME
);

CREATE TABLE dbo.ASCAUSUARIO (
  ID               VARCHAR(32) NOT NULL,
  NOMBRE           VARCHAR(254) NOT NULL,
  APELLIDOS        VARCHAR(254),
  EMAIL            VARCHAR(254),
  DIRECCION        VARCHAR(254),
  TIPO             CHAR(1)  NOT NULL,
  HABILITADO       CHAR (1) NOT NULL,
  FMAXVIGENCIA     DATETIME,
  IDUSRSEXTGESTOR  VARCHAR(64) NOT NULL,
  IDUSRSISTORG     VARCHAR(64),
  DESCRIPCION      VARCHAR(254)
);

CREATE TABLE dbo.ASGDDEPOSITO (
  ID              VARCHAR(32)  NOT NULL,
  IDTIPOELEMENTO  VARCHAR(32)  NOT NULL,
  NUMORDEN        SMALLINT NOT NULL,
  NOMBRE          VARCHAR(64) NOT NULL,
  UBICACION       VARCHAR(255) NOT NULL,
  MARCAS          INT NOT NULL,
  IDARCHIVO VARCHAR(32) NOT NULL
);

CREATE TABLE dbo.ASGDDEPOSITOELECTRONICO (
  ID              VARCHAR(32)  NOT NULL,
  IDEXT           VARCHAR(64)  NOT NULL,
  NOMBRE          VARCHAR(64)  NOT NULL,
  DESCRIPCION     VARCHAR(254),
  LOCALIZACION    TEXT           NOT NULL,
  USOFIRMA        VARCHAR(1)  DEFAULT 'N' NOT NULL
);

CREATE TABLE dbo.ASGDELEMASIG (
  ID              VARCHAR(32)  NOT NULL,
  NOMBRE          VARCHAR(64)  NOT NULL,
  IDTIPOELEMENTO  VARCHAR(32)  NOT NULL,
  NUMORDEN        INT   NOT NULL,
  IDELEMNAPADRE   VARCHAR(32)  NOT NULL,
  IDDEPOSITO      VARCHAR(32)  NOT NULL,
  LONGITUD        FLOAT    NOT NULL,
  IDFORMATO       VARCHAR(32)  NOT NULL,
  NUMHUECOS       SMALLINT    NOT NULL,
  CODORDEN        VARCHAR(50) NOT NULL DEFAULT ' '
);

CREATE TABLE dbo.ASGDELEMNOASIG (
  ID              VARCHAR(32)  NOT NULL,
  IDTIPOELEMENTO  VARCHAR(32)  NOT NULL,
  NUMORDEN        SMALLINT    NOT NULL,
  IDDEPOSITO      VARCHAR(32)  NOT NULL,
  NOMBRE          VARCHAR(64) NOT NULL,
  IDPADRE         VARCHAR(32),
  MARCAS          INT NOT NULL,
  CODORDEN        VARCHAR(50) NOT NULL DEFAULT ' '
);

CREATE TABLE dbo.ASGDHUECO (
  IDELEMAPADRE    VARCHAR(32)  NOT NULL,
  IDDEPOSITO      VARCHAR(32)  NOT NULL,
  NUMORDEN        SMALLINT        NOT NULL,
  ESTADO          CHAR (1) NOT NULL,
  IDFORMATO       VARCHAR(32) NOT NULL,
  IDUINSTALACION  VARCHAR(32),
  IDRELENTREGA    VARCHAR(32),
  FECHAESTADO     DATETIME NOT NULL,
  PATH            VARCHAR(512) NOT NULL,
  ORDENENRELACION SMALLINT 	DEFAULT 0,
  CODORDEN        VARCHAR(50) NOT NULL DEFAULT ' ',
  TIPOORD         SMALLINT NOT NULL DEFAULT 1,
  NUMERACION VARCHAR(16),
  MARCAS SMALLINT NOT NULL DEFAULT 0,
  IDUIREEACR	  VARCHAR(32)
);

CREATE TABLE dbo.ASGDSIGNUMORDEN (
  IDTIPOELEMPADRE  VARCHAR(32),
  IDELEMPADRE      VARCHAR(32),
  IDTIPOELEMENTO   VARCHAR(32) NOT NULL,
  NUMORDEN         SMALLINT NOT NULL
);

CREATE TABLE dbo.ASGDTIPOELEMENTO (
  ID           VARCHAR(32)  NOT NULL,
  NOMBRE       VARCHAR(64)  NOT NULL,
  NOMBREABREV  VARCHAR(16),
  IDPADRE      VARCHAR(32),
  ASIGNABLE    SMALLINT    NOT NULL,
  DESCRIPCION  VARCHAR(254),
  CARACTERORDEN CHAR(1) NOT NULL DEFAULT ' ',
  TIPOORD      SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.ASGDUDOCENUI (
  IDUNIDADDOC     VARCHAR(32),
  IDUINSTALACION  VARCHAR(32)  NOT NULL,
  POSUDOCENUI     SMALLINT    NOT NULL,
  SIGNATURAUDOC   VARCHAR(32)  NOT NULL,
  IDENTIFICACION  VARCHAR(254)  NOT NULL,
  IDUDOCRE        VARCHAR(32),
  DESCRIPCION     VARCHAR(254)
);

CREATE TABLE dbo.ASGDUINSTALACION (
  ID              VARCHAR(32)  NOT NULL,
  IDFORMATO       VARCHAR(32)  NOT NULL,
  SIGNATURAUI     VARCHAR(16)  NOT NULL,
  IDENTIFICACION  VARCHAR(254)  NOT NULL,
  MARCASBLOQUEO INT DEFAULT 0 NOT NULL,
  FCREACION DATETIME NOT NULL DEFAULT getDate()
);

CREATE TABLE dbo.ASGDHISTUINSTALACION(
	ID VARCHAR(32) NOT NULL,
	IDARCHIVO VARCHAR(32) NOT NULL,
	IDFORMATO VARCHAR(32) NOT NULL,
	SIGNATURAUI	VARCHAR(16) NOT NULL,
	IDENTIFICACION VARCHAR(254)	NOT NULL,
	FELIMINACION DATETIME NOT NULL,
	MOTIVO	SMALLINT NOT NULL
);

CREATE TABLE dbo.ASGFELEMENTOCF (
  ID             VARCHAR(32)  NOT NULL,
  TIPO           SMALLINT NOT NULL,
  IDNIVEL        VARCHAR(32) NOT NULL,
  CODIGO         VARCHAR(128),
  TITULO         VARCHAR(1024) NOT NULL,
  IDPADRE        VARCHAR(32),
  IDFONDO        VARCHAR(32),
  CODREFFONDO    VARCHAR(254),
  FINALCODREFPADRE  VARCHAR(1024),
  CODREFERENCIA  VARCHAR(1024),
  ESTADO         SMALLINT NOT NULL,
  IDELIMINACION  VARCHAR(32),
  IDFICHADESCR   VARCHAR(32),
  TIENEDESCR     CHAR (1) ,
  EDITCLFDOCS	  CHAR (1),
  IDREPECM			  VARCHAR(32),
  IDARCHIVO      VARCHAR(32),
  NIVELACCESO    SMALLINT NOT NULL,
  IDLCA          VARCHAR(32),
  SUBTIPO 		 SMALLINT NOT NULL DEFAULT 0,
  ORDPOS 		 VARCHAR(32)
);

CREATE TABLE dbo.ASGFENTPRODUCTORA (
  CODIGO          VARCHAR(32) NOT NULL,
  TIPO            SMALLINT  NOT NULL,
  SISTGESTORORG   VARCHAR(32),
  IDENTPRODSGORG  VARCHAR(64),
  IDDESCR         VARCHAR(32) NOT NULL
);

CREATE TABLE dbo.ASGFESTRUCTJNIVCF (
  IDNIVELPADRE    VARCHAR(32) NOT NULL,
  TIPONIVELPADRE  SMALLINT NOT NULL,
  IDNIVELHIJO     VARCHAR(32) NOT NULL,
  TIPONIVELHIJO   SMALLINT NOT NULL
);

CREATE TABLE dbo.ASGFFONDO (
  CODPAIS          VARCHAR(16) NOT NULL,
  CODCOMUNIDAD     VARCHAR(16) NOT NULL,
  CODARCHIVO       VARCHAR(32) NOT NULL,
  TIPO             SMALLINT NOT NULL,
  IDENTPRODUCTORA  VARCHAR(32) NOT NULL,
  IDELEMENTOCF     VARCHAR(32)  NOT NULL
);

CREATE TABLE dbo.ASGFNIVELCF (
  ID                VARCHAR(32)  NOT NULL,
  NOMBRE            VARCHAR(64)  NOT NULL,
  TIPO              SMALLINT    NOT NULL,
  IDFICHADESCRPREF  VARCHAR(32),
  IDFICHACLFDOCPREF VARCHAR(32),
  IDREPECMPREF 		VARCHAR(32),
  SUBTIPO 			SMALLINT NOT NULL DEFAULT 0
);

CREATE TABLE dbo.ASGFORGPROD (
  IDDESCR        VARCHAR(32)  NOT NULL,
  IDORG          VARCHAR(32)   NOT NULL,
  IDENTPRODINST  VARCHAR(32)   NOT NULL
);

CREATE TABLE dbo.ASGFPRODSERIE (
  IDSERIE          VARCHAR(32) NOT NULL,
  IDPROCEDIMIENTO  VARCHAR(32),
  IDPRODUCTOR      VARCHAR(32) NOT NULL,
  TIPOPRODUCTOR    SMALLINT NOT NULL,
  FECHAINICIO      DATETIME NOT NULL,
  FECHAFINAL       DATETIME,
  IDLCAPREF        VARCHAR(32),
  MARCAS INT DEFAULT 0
);

CREATE TABLE dbo.ASGFSERIE (
  IDELEMENTOCF         VARCHAR(32)  NOT NULL,
  IDFONDO              VARCHAR(32)  NOT NULL,
  ESTADOSERIE          SMALLINT NOT NULL,
  FECHAESTADO          DATETIME NOT NULL,
  ULTIMOESTADOAUTORIZ  SMALLINT,
  IDUSRGESTOR          VARCHAR(32),
  FEXTREMAINICIAL      DATETIME,
  FEXTREMAFINAL        DATETIME,
  IDPROCEDIMIENTO      VARCHAR(32),
  TIPOPROCEDIMIENTO	   SMALLINT,
  NUMUNIDADDOC         INT   NOT NULL,
  NUMUINSTALACION      INT   NOT NULL,
  VOLUMEN			   FLOAT NOT NULL,
  IDENTIFICACION       TEXT,
  OBSERVACIONES        VARCHAR(254),
  INFOFICHAUDOCREPECM    TEXT
);

CREATE TABLE dbo.ASGFVOLUMENSERIE (
	IDSERIE 		VARCHAR(32) NOT NULL,
	TIPODOCUMENTAL 	VARCHAR(254) NOT NULL,
	CANTIDAD 		FLOAT NOT NULL
	);

CREATE TABLE dbo.ASGFSOLICITUDSERIE (
  ID                VARCHAR(32) NOT NULL,
  IDSERIE           VARCHAR(32),
  ETIQUETASERIE     VARCHAR(254) NOT NULL,
  TIPO              SMALLINT NOT NULL,
  FECHA             DATETIME NOT NULL,
  INFO              VARCHAR(2000),
  MOTIVOSOLICITUD   VARCHAR(254),
  IDUSRSOLICITANTE  VARCHAR(32) NOT NULL,
  ESTADO            SMALLINT NOT NULL,
  FECHAESTADO       DATETIME NOT NULL,
  IDUSRGESTOR       VARCHAR(32),
  MOTIVORECHAZO     VARCHAR(254)
);

CREATE TABLE dbo.ASGFUNIDADDOC (
  IDELEMENTOCF      VARCHAR(32),
  IDSERIE           VARCHAR(32),
  IDFONDO           VARCHAR(32),
  TIPODOCUMENTAL	VARCHAR(254),
  NUMEXP            VARCHAR(256),
  CODSISTPRODUCTOR  VARCHAR(16),
  MARCASBLOQUEO     INT NOT NULL DEFAULT 0
);

CREATE TABLE dbo.ASGFSNSEC (
  NUMSEC  INT   NOT NULL
);

CREATE TABLE dbo.ASGFNUMSECVAL (
  IDSERIE  VARCHAR(32) NOT NULL,
  NUMSEC   INT  NOT NULL
);

CREATE TABLE dbo.ASGFNUMSECSEL (
  IDSERIE  VARCHAR(32)  NOT NULL,
  NUMSEC   INT NOT NULL
);

CREATE TABLE dbo.ASGFVALSERIE (
  ID                   VARCHAR(32)  NOT NULL,
  IDSERIE              VARCHAR(32)  NOT NULL,
  TITULO               VARCHAR(254)  NOT NULL,
  ESTADO               SMALLINT NOT NULL,
  FECHAESTADO          DATETIME NOT NULL,
  MOTIVORECHAZO        VARCHAR(254),
  ORDSERIEPRIMERNIVEL  SMALLINT,
  ORDSERIESEGNIVEL     SMALLINT,
  SERIESPRECEDENTES    TEXT,
  SERIESRELACIONADAS   TEXT,
  DOCSRECOPILATORIOS   VARCHAR(1024),
  VALORADMTIPO         SMALLINT,
  VALORADMVIG          SMALLINT,
  VALORADMJUST         VARCHAR(1024),
  VALORLEGALTIPO       SMALLINT,
  VALORLEGALVIG        SMALLINT,
  VALORLEGALJUST       VARCHAR(1024),
  VALORINFTIPO         SMALLINT,
  VALORINFJUST         VARCHAR(1024),
  VALORCIENTTIPO       SMALLINT,
  VALORCIENTJUST       VARCHAR(1024),
  VALORTECNTIPO        SMALLINT,
  VALORTECNJUST        VARCHAR(1024),
  VALORCULTTIPO        SMALLINT,
  VALORCULTJUST        VARCHAR(1024),
  TECNICAMUESTREO      SMALLINT,
  NUMREGISTRO          INT,
  FVALIDACION          DATETIME,
  FEVALUACION          DATETIME,
  FDICTAMEN            DATETIME,
  VALORDICTAMEN        SMALLINT,
  VALORDICTJUST        TEXT,
  FRESOLDICTAMEN       DATETIME,
  BOLETINDICTAMEN      VARCHAR(128),
  FBOLETINDICTAMEN     DATETIME,
  IDUSRGESTORSERIE     VARCHAR(32) NOT NULL,
  REGIMENACCESOTIPO    SMALLINT NOT NULL,
  REGIMENACCESOJUST    VARCHAR(1024),
  REGIMENACCESOVIG     SMALLINT,
  INFOSERIE 		   TEXT,
  REGIMENACCESOSUBTIPO SMALLINT NULL
);

CREATE TABLE dbo.ASGFELIMSERIE (
  ID               VARCHAR(32)  NOT NULL,
  IDVALORACION     VARCHAR(32)  NOT NULL,
  IDSERIE          VARCHAR(32)  NOT NULL,
  TITULO           VARCHAR(254)  NOT NULL,
  ESTADO           INT       NOT NULL,
  FECHAESTADO      DATETIME          NOT NULL,
  MOTIVORECHAZO    VARCHAR(254),
  NOTAS            VARCHAR(1024),
  MAXANOSVIGENCIA  INT       NOT NULL,
  CONDBUSQANADIDA  VARCHAR(1024),
  TIPOELIMINACION  INT       NOT NULL,
  SELECCIONUDOC    VARCHAR(1)  NOT NULL,
  FEJECUCION       DATETIME,
  FAPROBACION      DATETIME,
  FDESTRUCCION     DATETIME,
  FFINALIZACION    DATETIME,
  IDARCHIVO VARCHAR(32) NOT NULL
);

CREATE TABLE dbo.ASGFELIMUDOCCONS (
  IDELIMINACION  VARCHAR(32)  NOT NULL,
  IDUDOC         VARCHAR(32)  NOT NULL,
  SIGNATURAUDOC  VARCHAR(128)  NOT NULL,
  TITULOUDOC     VARCHAR(1024)  NOT NULL,
  FECHAINIUDOC   DATETIME,
  FECHAFINUDOC   DATETIME
);

CREATE TABLE dbo.ASGFELIMSELUDOC(
	IDELIMINACION VARCHAR(32) NOT NULL,
	IDUDOC VARCHAR(32) NOT NULL,
	IDFONDO VARCHAR(32),
	CODREFERENCIA VARCHAR(1024),
	CODIGO VARCHAR(128),
	SIGNATURAUDOC VARCHAR(32),
	EXPEDIENTEUDOC VARCHAR(256),
	TITULO VARCHAR(1024),
	CODSISTPRODUCTOR VARCHAR(16),
	TIPODOCUMENTAL VARCHAR(254),
	IDUINSTALACION VARCHAR(32),
	UBICACION VARCHAR(512),
	FECHAINIUDOC DATETIME,
	FECHAFINUDOC DATETIME,
	NUMERO VARCHAR(10)
);

CREATE TABLE dbo.ASGFHISTUDOC (
  IDELIMINACION   VARCHAR(32)  NOT NULL,
  IDUDOC          VARCHAR(32)  NOT NULL,
  SIGNATURAUDOC   VARCHAR(128)  NOT NULL,
  TITULOUDOC      VARCHAR(1024)  NOT NULL,
  NUMEXPUDOC      VARCHAR(256),
  CODREFUDOC      VARCHAR(1024)  NOT NULL,
  DATOSDESCRUDOC  TEXT
);

CREATE TABLE dbo.ASGPCONSULTA (
  ID                 VARCHAR(32)  NOT NULL,
  ANO                VARCHAR(4)  NOT NULL,
  ORDEN              INT   NOT NULL,
  TIPOENTCONSULTORA  SMALLINT    NOT NULL,
  NORGCONSULTOR      VARCHAR(254),
  NUSRCONSULTOR      VARCHAR(254)  NOT NULL,
  FINICIALRESERVA    DATETIME,
  FENTREGA           DATETIME,
  FMAXFINCONSULTA    DATETIME,
  ESTADO             SMALLINT    NOT NULL,
  FESTADO            DATETIME          NOT NULL,
  MOTIVO             VARCHAR(254),
  IDARCHIVO          VARCHAR(32)  NOT NULL,
  IDUSRSOLICITANTE   VARCHAR(32)  NOT NULL,
  INFORMACION        VARCHAR(1024),
  TEMA               VARCHAR(254),
  TIPO               SMALLINT    NOT NULL,
  TIPOENTREGA 	     VARCHAR(254),
  DATOSAUTORIZADO    VARCHAR(254),
  DATOSSOLICITANTE   VARCHAR(512),
  OBSERVACIONES VARCHAR(254),
  IDMOTIVO VARCHAR(32),
  IDUSRCSALA VARCHAR(32)
);

CREATE TABLE dbo.ASGPMTVCONSULTA (
  ID 			VARCHAR(32) NOT NULL,
  TIPOENTIDAD   SMALLINT    NOT NULL,
  MOTIVO        VARCHAR(254)  NOT NULL,
  TIPOCONSULTA  SMALLINT NOT NULL,
  VISIBILIDAD SMALLINT
);

CREATE TABLE dbo.ASGPMTVRECHAZO (
  ID 			 VARCHAR(32) NOT NULL,
  TIPOSOLICITUD  SMALLINT    NOT NULL,
  MOTIVO         VARCHAR(254)  NOT NULL
);

CREATE TABLE dbo.ASGPPRESTAMO (
  ID                VARCHAR(32)  NOT NULL,
  ANO               VARCHAR(4)  NOT NULL,
  ORDEN             INT   NOT NULL,
  NORGSOLICITANTE   VARCHAR(254)  NOT NULL,
  NUSRSOLICITANTE   VARCHAR(254)  NOT NULL,
  IDUSRSOLICITANTE  VARCHAR(32),
  FINICIALRESERVA   DATETIME,
  IDORGSOLICITANTE  VARCHAR(32),
  FENTREGA          DATETIME,
  FMAXFINPRESTAMO   DATETIME,
  ESTADO            SMALLINT    NOT NULL,
  FESTADO           DATETIME          NOT NULL,
  IDARCHIVO         VARCHAR(32)  NOT NULL,
  IDUSRGESTOR       VARCHAR(32)  NOT NULL,
  NUMRECLAMACIONES  INT   NOT NULL,
  FRECLAMACION1     DATETIME,
  FRECLAMACION2     DATETIME,
  TIPOENTREGA 	    VARCHAR(254),
  DATOSAUTORIZADO   VARCHAR(254),
  DATOSSOLICITANTE  VARCHAR(512),
  OBSERVACIONES 	VARCHAR(254),
  IDMOTIVO 			VARCHAR(32)
);

CREATE TABLE dbo.ASGPMTVPRESTAMO (
	ID            VARCHAR(32) NOT NULL,
	TIPOUSUARIO   SMALLINT    NOT NULL,
	MOTIVO        VARCHAR(254)  NOT NULL,
	VISIBILIDAD   SMALLINT
	);

CREATE TABLE dbo.ASGPPRORROGA (
  ID             VARCHAR(32)  NOT NULL,
  IDPRESTAMO     VARCHAR(32)  NOT NULL,
  ESTADO         SMALLINT    NOT NULL,
  FESTADO        DATETIME          NOT NULL,
  MOTIVORECHAZO  VARCHAR(254),
  IDMOTIVO VARCHAR(32),
  FECHAFINPRORROGA DATETIME,
  MOTIVOPRORROGA VARCHAR(512)
);

CREATE TABLE dbo.ASGPSNSEC (
  TIPO    INT   NOT NULL,
  ANO     VARCHAR(4)  NOT NULL,
  NUMSEC  INT   NOT NULL
);

CREATE TABLE dbo.ASGPSOLICITUDUDOC (
  IDSOLICITUD     VARCHAR(32)  NOT NULL,
  TIPOSOLICITUD   SMALLINT    NOT NULL,
  IDUDOC          VARCHAR(32)  NOT NULL,
  TITULO          VARCHAR(1024)  NOT NULL,
  SIGNATURAUDOC   VARCHAR(254)  NOT NULL,
  EXPEDIENTEUDOC  VARCHAR(128),
  IDENTIFICACION  VARCHAR(254)  NOT NULL,
  ESTADO          SMALLINT    NOT NULL,
  FESTADO         DATETIME          NOT NULL,
  FINICIALUSO     DATETIME,
  FFINALUSO       DATETIME,
  MOTIVORECHAZO   VARCHAR(254),
  INFORMACION     VARCHAR(1024),
  IDFONDO         VARCHAR(32) NOT NULL,
  IDMOTIVO        VARCHAR(32)
);

CREATE TABLE dbo.ASGPTEMA (
  IDUSUARIO    VARCHAR(32),
  TIPOENTIDAD  SMALLINT    NOT NULL,
  TEMA         VARCHAR(254)  NOT NULL,
  IDUSRCSALA   VARCHAR(32)
);

CREATE TABLE dbo.ASGTDETALLEPREV (
  ID                   VARCHAR(32)  NOT NULL,
  IDPREVISION          VARCHAR(32) NOT NULL,
  ORDEN                SMALLINT NOT NULL,
  CODSISTPRODUCTOR     VARCHAR(16),
  NOMBRESISTPRODUCTOR  VARCHAR(64),
  IDPROCEDIMIENTO      VARCHAR(32),
  IDSERIEDESTINO       VARCHAR(32) NOT NULL,
  ANOINIUDOC           CHAR (4) NOT NULL,
  ANOFINUDOC           CHAR (4) NOT NULL,
  NUMUINSTALACION      SMALLINT NOT NULL,
  IDFORMATOUI          VARCHAR(32) NOT NULL,
  NUMRENTREGA          SMALLINT,
  CERRADO              CHAR (1) NOT NULL,
  OBSERVACIONES        VARCHAR(254),
  IDSERIEORIGEN VARCHAR(32),
  INFO VARCHAR(1024)
);

CREATE TABLE dbo.ASGTPREVISION (
  ID                 VARCHAR(32) NOT NULL,
  TIPOTRANSFERENCIA  INT NOT NULL,
  TIPOPREVISION      INT NOT NULL,
  IDORGREMITENTE     VARCHAR(32) NOT NULL,
  ANO                VARCHAR(4) NOT NULL,
  ORDEN              INT NOT NULL,
  IDFONDODESTINO     VARCHAR(32) NOT NULL,
  FECHAINITRANS      DATETIME,
  FECHAFINTRANS      DATETIME,
  NUMUINSTALACION    SMALLINT,
  ESTADO             SMALLINT NOT NULL,
  FECHAESTADO        DATETIME NOT NULL,
  IDARCHIVORECEPTOR  VARCHAR(32) NOT NULL,
  IDUSRGESTOR        VARCHAR(32) NOT NULL,
  MOTIVORECHAZO      VARCHAR(254),
  OBSERVACIONES      VARCHAR(254),
  IDARCHIVOREMITENTE VARCHAR(32)
);

CREATE TABLE dbo.ASGTRENTREGA (
  ID                   VARCHAR(32)  NOT NULL,
  TIPOTRANSFERENCIA    SMALLINT NOT NULL,
  IDPREVISION          VARCHAR(32) NOT NULL,
  IDDETPREVISION       VARCHAR(32),
  IDORGANOREMITENTE    VARCHAR(32) NOT NULL,
  ANO                  CHAR (4) NOT NULL,
  ORDEN                INT NOT NULL,
  IDFONDODESTINO       VARCHAR(32) NOT NULL,
  CODSISTPRODUCTOR     VARCHAR(16),
  NOMBRESISTPRODUCTOR  VARCHAR(64),
  IDPROCEDIMIENTO      VARCHAR(32),
  IDFORMATOUI          VARCHAR(32) NOT NULL,
  TIPODOCUMENTAL       VARCHAR(254),
  ESTADO               SMALLINT NOT NULL,
  FECHAESTADO          DATETIME NOT NULL,
  IDARCHIVORECEPTOR    VARCHAR(32) NOT NULL,
  FECHARECEPCION       DATETIME,
  IDUSRGESTORREM       VARCHAR(32) NOT NULL,
  IDUSRGESTORARCHIVOREC   VARCHAR(32),
  IDDEPOSITO           VARCHAR(32),
  RESERVADEPOSITO      SMALLINT,
  UIENDEPOSITO         CHAR (1),
  DEVOLUCIONUI         CHAR (1),
  REGENTRADA           VARCHAR(64),
  OBSERVACIONES        VARCHAR(2048),
  IDSERIEDESTINO       VARCHAR(32) NOT NULL,
  IDELMTODRESERVA      VARCHAR(32),
  IDDESCRORGPRODUCTOR  VARCHAR(32),
  IDTIPOELMTODRESERVA  VARCHAR(32),
  IDARCHIVOREMITENTE 	VARCHAR(32),
  IDFONDOORIGEN 		VARCHAR(32),
  IDSERIEORIGEN 		VARCHAR(32),
  CORRECCIONENARCHIVO 	CHAR(1) NOT NULL DEFAULT 'N',
  IDNIVELDOCUMENTAL 	VARCHAR(32) NOT NULL,
  SINDOCSFISICOS 		CHAR (1),
  IDFICHA VARCHAR(32),
  CONREENCAJADO CHAR(1) NOT NULL DEFAULT 'N',
  IDFORMATORE VARCHAR(32)
);

CREATE TABLE dbo.ASGTSNSEC (
  TIPO    SMALLINT  NOT NULL,
  ANO     CHAR (4)  NOT NULL,
  NUMSEC  INT  NOT NULL,
  IDARCHIVO  VARCHAR(32) NOT NULL
);

CREATE TABLE dbo.ASGTSNSECUI (
  NUMSEC  INT NOT NULL,
  IDARCHIVO VARCHAR(32)
);

CREATE TABLE dbo.ASGTSNSECUDOC(
  NUMSEC  INT NOT NULL
);

CREATE TABLE dbo.ASGTUDOCENUI (
  IDRELENTREGA      VARCHAR(32)     NOT NULL,
  IDUNIDADDOC       VARCHAR(32) NOT NULL,
  IDUINSTALACIONRE  VARCHAR(32) NOT NULL,
  POSUDOCENUI       SMALLINT NOT NULL,
  UDOCCOMPLETA      CHAR (1) NOT NULL,
  ESTADOCOTEJO      SMALLINT NOT NULL,
  NOTASCOTEJO       VARCHAR(254),
  SIGNATURAUDOC     VARCHAR(32),
  NUMPARTEUDOC      SMALLINT NOT NULL,
  DESCCONTENIDO     VARCHAR(254)
);

CREATE TABLE dbo.ASGTUINSTALACIONRE (
  ID            VARCHAR(32)  NOT NULL,
  IDRELENTREGA  VARCHAR(32) NOT NULL,
  ORDEN         SMALLINT NOT NULL,
  IDFORMATO     VARCHAR(32) NOT NULL,
  SIGNATURAUI   VARCHAR(16),
  ESTADOCOTEJO  SMALLINT NOT NULL,
  NOTASCOTEJO   VARCHAR(254),
  DEVOLUCION    CHAR (1) NOT NULL,
  IDUIUBICADA 	VARCHAR(32)
);

CREATE TABLE dbo.ASGTUINSTALACIONREEA(
  IDUIDEPOSITO 	VARCHAR(32) NOT NULL,
  IDRELENTREGA 	VARCHAR(32) NOT NULL,
  ORDEN 	SMALLINT NOT NULL,
  IDFORMATO 	VARCHAR(32) NOT NULL,
  ESTADOCOTEJO	SMALLINT NOT NULL,
  NOTASCOTEJO 	VARCHAR(254),
  DEVOLUCION 	CHAR(1) NOT NULL,
  PATHDEPOSITOORIGEN VARCHAR(512) NOT NULL,
  SIGNATURAUI VARCHAR(16),
  IDELEMAPADREHUECOORIGEN VARCHAR(32) NOT NULL,
  NUMORDENHUECOORIGEN SMALLINT NOT NULL,
  SIGNATURAUIORIGEN VARCHAR(16)
);

CREATE TABLE dbo.ASGTUNIDADDOCRE (
  ID                VARCHAR(32)  NOT NULL,
  IDRELENTREGA      VARCHAR(32) NOT NULL,
  TIPOTRANSFERENCIA SMALLINT NOT NULL,
  NUMEXP            VARCHAR(256),
  CODSISTPRODUCTOR  VARCHAR(16),
  FECHAEXTINI       DATETIME,
  FECHAEXTFIN       DATETIME,
  ASUNTO            VARCHAR(1024),
  VALIDADA          CHAR (1) NOT NULL,
  SINDOCSFISICOS    CHAR (1) NOT NULL,
  INFO              TEXT NOT NULL,
  NUMPARTES         SMALLINT,
  ORDEN             INT,
  MARCASBLOQUEO     INT NOT NULL DEFAULT 0
);

CREATE TABLE dbo.ASGTUDOCSDF(
	ID VARCHAR(32) NOT NULL,
	IDRELENTREGA VARCHAR(32) NOT NULL,
	POSICION SMALLINT NOT NULL,
	ESTADOCOTEJO SMALLINT NOT NULL,
	NOTASCOTEJO VARCHAR(254),
);

CREATE TABLE dbo.ASGFUDOCSDF (
	IDELEMENTOCF VARCHAR(32) NOT NULL,
	MARCASBLOQUEO SMALLINT DEFAULT 0 NOT NULL
);

CREATE TABLE dbo.ASGTMAPDESCRUDOC (
	IDFICHA VARCHAR(32)  NOT NULL,
	INFO	TEXT  NOT NULL
);

CREATE TABLE dbo.ITDGUIDGEN (
  CNODE  VARCHAR(12),
  CLPID  INT
);

CREATE TABLE dbo.AGINFOSISTEMA (
  AUTID INT IDENTITY(1,1),
  NOMBRE VARCHAR(32) NOT NULL,
  VALOR TEXT NOT NULL,
  FECHAACTUALIZACION DATETIME NOT NULL
);

CREATE TABLE dbo.ADVCFECHAUDOCRE (
	IDUDOCRE VARCHAR(32) NOT NULL,
	IDCAMPO VARCHAR(32) NOT NULL,
	VALOR VARCHAR(64),
	FECHAINI DATETIME,
	FECHAFIN DATETIME,
	FORMATO VARCHAR(16),
	SEP VARCHAR(1),
	CALIFICADOR VARCHAR(32),
	ORDEN INT NOT NULL,
	TIPOUDOC SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.ADVCNUMUDOCRE (
	IDUDOCRE VARCHAR(32) NOT NULL,
	IDCAMPO VARCHAR(32) NOT NULL,
	VALOR DECIMAL(8,2) NOT NULL,
	ORDEN INT NOT NULL,
	TIPOMEDIDA INT,
	UNIDADMEDIDA VARCHAR(16),
	TIPOUDOC SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.ADVCREFUDOCRE (
	IDUDOCRE VARCHAR(32) NOT NULL,
	IDCAMPO VARCHAR(32) NOT NULL,
	TIPOOBJREF INT NOT NULL,
	IDOBJREF VARCHAR(32) NOT NULL,
	ORDEN INT NOT NULL,
	TIPOUDOC SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.ADVCTEXTLUDOCRE (
	IDUDOCRE VARCHAR(32) NOT NULL,
	IDCAMPO VARCHAR(32) NOT NULL,
	VALOR TEXT NOT NULL,
	ORDEN INT NOT NULL,
	TIPOUDOC SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.ADVCTEXTUDOCRE (
	IDUDOCRE VARCHAR(32) NOT NULL,
	IDCAMPO VARCHAR(32) NOT NULL,
	VALOR VARCHAR(254) NOT NULL,
	ORDEN INT NOT NULL,
	TIPOUDOC SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.ASGFDIVISIONFS (
   IDFS VARCHAR(32) NOT NULL,
   IDFICHA VARCHAR(32) NULL,
   IDNIVELDOCUMENTAL VARCHAR(32) NOT NULL,
   IDUSRGESTOR VARCHAR(32) NOT NULL,
   ESTADO SMALLINT NOT NULL,
   INFO VARCHAR(1024),
   FECHAESTADO datetime NOT NULL
);

CREATE TABLE dbo.ASGFUDOCENDIVISIONFS (
	IDFS 			VARCHAR(32) NOT NULL,
	IDUDOC 			VARCHAR(32) NOT NULL,
	NUMEXP          VARCHAR(256),
	FECHAEXTINI     DATETIME NOT NULL,
  	FECHAEXTFIN     DATETIME NOT NULL,
  	ASUNTO          VARCHAR(1024) NOT NULL,
  	ORDEN			INT NOT NULL,
  	VALIDADA 		CHAR(1) NOT NULL,
  	INFO            TEXT
  	);

CREATE TABLE dbo.ASGFPZTRVALSERIE(
	IDVALSERIE VARCHAR(32) NOT NULL,
	PLAZO 			SMALLINT  NOT NULL,
	IDNIVELARCHORG 	VARCHAR(32) NOT NULL,
	IDNIVELARCHDST 	VARCHAR(32) NOT NULL,
	ORDEN 			SMALLINT NOT NULL
);

CREATE TABLE dbo.ASGDSIGNUMHUECO (
  IDARCHIVO 	VARCHAR(32) NOT NULL,
  SIGNUMHUECO  	BIGINT NOT NULL
);

CREATE TABLE dbo.ASGPREVDOCUDOC (
  IDREVDOC			VARCHAR(32)  NOT NULL,
  IDUDOC          		VARCHAR(32)  NOT NULL,
  TITULO          		VARCHAR(1024)  NOT NULL,
  SIGNATURAUDOC   	VARCHAR(254)  NOT NULL,
  EXPEDIENTEUDOC  	VARCHAR(128),
  ESTADO          		SMALLINT    NOT NULL,
  FESTADO         		DATETIME          NOT NULL,
  OBSERVACIONES        	VARCHAR(1024),
  MOTIVORECHAZO 	VARCHAR(254),
  IDUSRGESTOR        	VARCHAR(32) NOT NULL,
  IDALTA			VARCHAR(32)
);

CREATE TABLE dbo.ASGSEDIFICIO (
  ID			VARCHAR(32) NOT NULL,
  NOMBRE        VARCHAR(64) NOT NULL,
  UBICACION     VARCHAR(254),
  IDARCHIVO   	VARCHAR(32) NOT NULL
);

CREATE TABLE dbo.ASGSSALA (
  ID	 	  	  		VARCHAR(32) NOT NULL,
  NOMBRE          		VARCHAR(64) NOT NULL,
  DESCRIPCION     		VARCHAR(254),
  IDEDIFICIO   	  		VARCHAR(32) NOT NULL,
  EQUIPOINFORMATICO     CHAR(1) NOT NULL
);

CREATE TABLE dbo.ASGSMESA (
  ID			VARCHAR(32) NOT NULL,
  CODIGO        VARCHAR(64) NOT NULL,
  NUMORDEN		INT NOT NULL,
  IDSALA		VARCHAR(32) NOT NULL,
  ESTADO		CHAR (1) NOT NULL,
  FECHAESTADO   DATETIME NOT NULL,
  IDUSRCSALA	VARCHAR(32)
);

CREATE TABLE dbo.ASGSUSRCSALA (
  ID	 	  	  			VARCHAR(32) NOT NULL,
  TIPODOCIDENTIFICACION     INT NOT NULL,
  NUMDOCIDENTIFICACION		VARCHAR(32) NOT NULL,
  NOMBRE			  	    VARCHAR(64) NOT NULL,
  APELLIDOS			  	    VARCHAR(254) NOT NULL,
  NACIONALIDAD	   	  	    VARCHAR(64),
  TELEFONOS					VARCHAR(128),
  EMAIL						VARCHAR(128),
  DIRECCION		            VARCHAR(254),
  FECHAALTA				    DATETIME NOT NULL,
  VIGENTE					CHAR (1) NOT NULL,
  IDSCAUSR					VARCHAR(32)
);

CREATE TABLE dbo.ASGSUSRCSARCH (
  IDUSRCSALA  	  VARCHAR(32) NOT NULL,
  IDARCHIVO       VARCHAR(64) NOT NULL
);

CREATE TABLE dbo.ASGSREGCSALA (
  ID		  	  		VARCHAR(32) NOT NULL,
  IDUSRCSALA  	  		VARCHAR(32) NOT NULL,
  FENTRADA	      		DATETIME NOT NULL,
  FSALIDA	      		DATETIME,
  NUMDOCIDENTIFICACION	VARCHAR(32) NOT NULL,
  NOMBREAPELLIDOS		VARCHAR(254) NOT NULL,
  IDSCAUSR  	  		VARCHAR(32),
  IDARCHIVO  	  		VARCHAR(32) NOT NULL,
  MESAASIGNADA			VARCHAR(254) NOT NULL
);

CREATE TABLE dbo.ASGTUIREEACR (
  ID			  VARCHAR(32)  NOT NULL,
  IDRELENTREGA    VARCHAR(32)  NOT NULL,
  IDUIDEPOSITO    VARCHAR(32),
  SIGNATURAUI     VARCHAR(16),
  IDFORMATO       VARCHAR(32) NOT NULL,
  ORDEN			  SMALLINT NOT NULL,
  ESTADOCOTEJO 	  SMALLINT NOT NULL	DEFAULT 1,
  NOTASCOTEJO	  VARCHAR(256),
  DEVOLUCION	  CHAR (1) DEFAULT 'N',
  DESCRIPCION	  VARCHAR(256)
);

CREATE TABLE dbo.ASGTUDOCENUIREEACR (
  ID			  VARCHAR(32)  NOT NULL,
  IDUDOCORIGEN    VARCHAR(32)  NOT NULL,
  IDRELENTREGA    VARCHAR(32)  NOT NULL,
  IDUNIDADDOC     VARCHAR(32)  NOT NULL,
  IDUIREEACR	  VARCHAR(32),
  ASUNTO		  VARCHAR(1024) NOT NULL,
  UDOCCOMPLETA	  VARCHAR(1) NOT NULL,
  SIGNATURAUDOC   VARCHAR(32),
  POSUDOCENUI     SMALLINT,
  NUMEXP          VARCHAR(256),
  NUMPARTE        SMALLINT,
  FECHAINI        VARCHAR(64),
  FECHAFIN        VARCHAR(64),
  SIGNATURAUDOCORIGEN VARCHAR(32),
  DESCRIPCION	  VARCHAR(256)
);

CREATE TABLE dbo.ASGTUDOCREEACR (
  ID			  VARCHAR(32)  NOT NULL,
  IDRELENTREGA    VARCHAR(32)  NOT NULL,
  IDUNIDADDOC     VARCHAR(32)  NOT NULL,
  SIGNATURAUDOC	  VARCHAR(32)  NOT NULL,
  IDUIDEPOSITO	  VARCHAR(32)  NOT NULL,
  SIGNATURAUI	  VARCHAR(16)  NOT NULL
);

CREATE TABLE dbo.ASGFCTLGTBLTMP(
	ID 			SMALLINT	NOT NULL,
	NOMBRETABLA VARCHAR(16) NOT NULL,
	IDUSUARIO 	VARCHAR(32),
	ESTADO 		SMALLINT 	DEFAULT 0,
	FECHA 		DATETIME
);--NOTA: Para esta funcionalidad necesario permiso de db_ddladmin o permisos para crear tablas. ya que la aplicacion necesita crear tablas en tiempo de ejecucion


COMMIT TRANSACTION;