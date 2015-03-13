# Introducción #

La finalidad de jPPersistor es brindar todas las herramientas necesarias para almacenar y cargar sencillamente escenarios de juego en archivos XML.
La clase principal es XMLPersistor y debe utilizársela del siguiente modo:

## Almacenar un juego ##

```
XMLPersistor persistor = new XMLPersistor();
persistor.save(gameEngine, "savegame001.xml");
```

## Cargar un juego ##

```
XMLPersistor persistor = new XMLPersistor();
GameEngine gameEngine = persistor.load("savegame001.xml", myGameView, myGameController);
```