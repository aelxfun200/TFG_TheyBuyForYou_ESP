# TFG_TheyBuyForYou_ESP

IDEA PRINCIPAL DEL TFG: 
		
+ REFINAR LOS DATOS QUE HAY DISPONIBLES EN EL KNOWLEDGE GRAPH DE THEYBUYFORYOU PARA CENTRARSE EN LAS LICITACIONES EN ESPAÑA PARA CREAR UN SUBCONJUNTO DEL KNOWLEDGE GRAPH

+ COMPROBAR INICIALMENTE EL GRADO DE COBERTURA QUE TIENE CON RESPECTO A LOS DATOS QUE HAY EN LA PLATAFORMA DE CONTRATACIÓN DEL ESTADO

+ REVISAR CÓMO ARREGLAR ERRORES
    

Puntos de análisis iniciales

- Extraer datos de la plataforma de contratación del Estado: https://contrataciondelestado.es/wps/portal/plataforma 
	+ Ubicación de los datos públicos: https://www.hacienda.gob.es/es-ES/GobiernoAbierto/Datos%20Abiertos/Paginas/licitaciones_plataforma_contratacion.aspx
		
	+ Código empleado para extraer los datos: https://github.com/ocorcho/Extractor_Xml2Tables
						https://github.com/pproc/pproc-pcsp

- Comparación y análisis de datos de ontología PPROC (estándar de modelo para tratamiento de datos de contratación en España)
	+ Descripción de ontologías:  http://pproc.unizar.es/def/sector-publico/pproc.html (se ve mal debido a código CSS)
				    http://www.semantic-web-journal.net/system/files/swj1142.pdf					   
				    https://github.com/pproc	

- Análisis de ontologías y grafos de Proyecto TheyBuyForYou. Siguen la línea de la ontología OCDS (Open Contracting Data Standard): https://standard.open-contracting.org/latest/en/
	+ Descripción de ontología OCDS: https://github.com/TBFY/ocds-ontology/blob/master/model/ocds-extended.ttl
	+ Descripción de grafo TheyBuyForYou: https://zenodo.org/record/4498267#.YDfGG2pKjok
					      https://github.com/TBFY/

Puntos de desarrollo

- Software actualizado para obtener los datos de los XML de la Plataforma de Contratación del Estado (que se publican cada día o cada x tiempo)

- Analizar las relaciones entre el modelo de CODICE(Modelo que se utiliza en España para trabajar con la Plataforma de Contratación del Estado) y OCDS, con el fin de sacar un listado con lo que se puede respresentar y lo que no.

- Refinar el grafo de TheyBuyForYou para extaer únicamente la parte correspondiente a España, mediante un proceso que se puede ejecutar diartiamente

- Comparación y enlazado entre TheyBuyForYou y la Plataforma de Contratación del Estado (Adicionalmente podría involucrar obtener documentos asociados a las licitaciones, o información adicional de los portales de contratación enlazados de cada ciudad o comunidad)                                              
