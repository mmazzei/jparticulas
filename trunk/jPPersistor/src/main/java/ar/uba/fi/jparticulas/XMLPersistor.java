package ar.uba.fi.jparticulas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ar.uba.fi.jparticulas.adapter.Persistible;
import ar.uba.fi.jparticulas.adapter.PersistibleFactory;
import ar.uba.fi.jparticulas.adapter.VectorPersistibleAdapter;
import ar.uba.fi.jparticulas.controller.GameController;
import ar.uba.fi.jparticulas.controller.GameEngine;
import ar.uba.fi.jparticulas.model.Ambiente;
import ar.uba.fi.jparticulas.view.GameView;
import ar.uba.fi.jparticulas.view.ViewFactory;
import ar.uba.fi.jparticulas.view.movil.MovilView;

// TODO (mmazzei) - Averiguar por qué maven no crea el JAR si quito estos dos import
import ar.uba.fi.jparticulas.model.movil.*;
import ar.uba.fi.jparticulas.model.movil.complex.*;

/**
 * Clase encargada de almacenar en formato XML y de recuperar toda la información correspondiente a
 * un {@link GameEngine}.
 * 
 * @author mmazzei
 */
public class XMLPersistor {
	// Nombre dado a los diferentes tags del XML
	public static final String ROOT_NAME = "game";
	public static final String AMBIENTE_NAME = "ambiente";
	public static final String OBSTACLES_NAME = "obstacles";
	public static final String COLLIDER_PARTICLES_NAME = "colliderParticles";
	public static final String NON_COLLIDER_PARTICLES_NAME = "nonColliderParticles";
	public static final String TEMPORAL_PARTICLES_NAME = "temporalParticles";

	public void save(GameEngine engine, String fileName) throws IOException {
		Element root = new Element(ROOT_NAME);

		// Almaceno el ambiente
		root.addContent(this.saveAmbiente(engine.getAmbiente(), AMBIENTE_NAME));

		// Almaceno todos los móviles del juego
		root.addContent(saveMovilCollection(engine.getObstacles(), OBSTACLES_NAME));
		root.addContent(saveMovilCollection(engine.getColliderParticles(), COLLIDER_PARTICLES_NAME));
		root.addContent(saveMovilCollection(engine.getNonColliderParticles(), NON_COLLIDER_PARTICLES_NAME));
		root.addContent(saveMovilCollection(engine.getTemporalParticles(), TEMPORAL_PARTICLES_NAME));

		// Genero y escribo el documento
		Document document = new Document(root);
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		FileOutputStream file = new FileOutputStream(fileName);
		out.output(document, file);
		file.flush();
		file.close();
	}

	public GameEngine load(String fileName, GameView view, GameController controller) throws JDOMException, IOException {
		// Obtengo la raiz del documento
		FileInputStream file = new FileInputStream(fileName);
		SAXBuilder parser = new SAXBuilder();
		Document doc = parser.build(file);

		Element root = doc.getRootElement();
		GameEngine gameEngine = new GameEngine(view, controller);

		// Cargo el ambiente
		gameEngine.setAmbiente(this.loadAmbiente(root.getChild(AMBIENTE_NAME)));

		// Cargo los móviles del escenario
		gameEngine.setObstacles(this.loadMovilCollection(gameEngine.getAmbiente(), root.getChild(OBSTACLES_NAME)));
		gameEngine.setColliderParticles(this.loadMovilCollection(gameEngine.getAmbiente(), root
				.getChild(COLLIDER_PARTICLES_NAME)));
		gameEngine.setNonColliderParticles(this.loadMovilCollection(gameEngine.getAmbiente(), root
				.getChild(NON_COLLIDER_PARTICLES_NAME)));
		gameEngine.setTemporalParticles(this.loadMovilLinkedList(gameEngine.getAmbiente(), root
				.getChild(TEMPORAL_PARTICLES_NAME)));

		file.close();
		return gameEngine;
	}

	/**
	 * Almacena en un elemento JDOM todos los móviles de una colección.
	 * 
	 * @param collection
	 *            Conjunto de móviles a almacenar.
	 * @param name
	 *            Nombre del elemento a generar.
	 * @return El elemento con la información de todos los móviles.
	 */
	private Element saveMovilCollection(Collection<MovilView> collection, String name) {
		Element eCollection = new Element(name);
		for (MovilView vMovil : collection) {
			Persistible pMovil = PersistibleFactory.create(vMovil.getMovil());
			eCollection.addContent(pMovil.save(pMovil.getType()));
		}
		return eCollection;
	}

	/**
	 * @param ambiente
	 *            El ambiente del escenario.
	 * @param element
	 *            El elemento JDOM que contiene la información a cargar.
	 * @return La colección de objetos representada por el elemento JDOM, con el ambiente indicado.
	 * @throws DataConversionException
	 */
	private ArrayList<MovilView> loadMovilCollection(Ambiente ambiente, Element element) throws DataConversionException {
		ArrayList<MovilView> moviles = new ArrayList<MovilView>();
		for (Object oElement : element.getChildren()) {
			Persistible persistible = PersistibleFactory.create((Element) oElement);
			moviles.add(ViewFactory.create(persistible.getMovil()));
		}

		return moviles;
	}

	/**
	 * Idéntico a {@link #loadMovilCollection(Ambiente, Element)} salvo por el tipo de colección
	 * retornado.
	 */
	private LinkedList<MovilView> loadMovilLinkedList(Ambiente ambiente, Element element)
			throws DataConversionException {
		LinkedList<MovilView> moviles = new LinkedList<MovilView>();
		for (Object oElement : element.getChildren()) {
			Persistible persistible = PersistibleFactory.create((Element) oElement);
			moviles.add(ViewFactory.create(persistible.getMovil()));
		}

		return moviles;
	}

	/**
	 * Almacena en un elemento JDOM la información de un ambiente.
	 * 
	 * @param ambiente
	 *            El ambiente a almacenar.
	 * @param name
	 *            Nombre del elemento a generar.
	 * @return El elemento con la información del ambiente.
	 */
	// TODO (mmazzei) - Deshardcodear
	private Element saveAmbiente(Ambiente ambiente, String name) {
		Element eAmbiente = new Element(name);
		VectorPersistibleAdapter acceleration = new VectorPersistibleAdapter(ambiente.getAcceleration());
		eAmbiente.addContent(acceleration.save("acceleration"));
		eAmbiente.setAttribute("umbralPrecision", ambiente.getUmbralPrecision() + "");
		eAmbiente.setAttribute("viscosity", ambiente.getViscosity() + "");

		return eAmbiente;
	}

	/**
	 * @param element
	 *            El elemento JDOM que contiene la información a cargar.
	 * @return El ambiente cuya información se almacenó en el elemento JDOM.
	 */
	// TODO (mmazzei) - Deshardcodear
	private Ambiente loadAmbiente(Element element) throws DataConversionException {
		double viscosity = element.getAttribute("viscosity").getDoubleValue();
		VectorPersistibleAdapter acceleration = new VectorPersistibleAdapter(element.getChild("acceleration"));
		double umbralPrecision = element.getAttribute("umbralPrecision").getDoubleValue();

		return new Ambiente(viscosity, acceleration.getVector(), umbralPrecision);
	}
}
