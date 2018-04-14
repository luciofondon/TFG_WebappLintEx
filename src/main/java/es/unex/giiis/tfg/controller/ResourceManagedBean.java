package es.unex.giiis.tfg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.model.ResourceDTO.TypeResource;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.ResourceService;

@Controller
@ManagedBean
@SessionScoped
public class ResourceManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("ResourceManagedBean");

	private TreeNode root;

	private TreeNode selectedNode;

	@Autowired
	private ResourceService service;

	// Recursos sincronizados y ya descargados
	private List<ResourceDTO> listResourceWeb;

	// Rutas de los recursos en Android
	private List<ResourceDTO> listResourceAndroid;
	
	private List<ResourceDTO> listFilteredAndroid;

	private ResourceDTO resourceSelected;

	public List<ResourceDTO> getListResourceWeb() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listResourceWeb = this.service.findAllResourceByImeiAndTypeResource(imei.toString(),
						TypeResource.WEB);
			} catch (ServiceException e) {
				this.LOGGER.info("" + e);
			}
		}
		return this.listResourceWeb;
	}

	public List<ResourceDTO> getListResourceAndroid() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listResourceAndroid = this.service.findAllResourceByImeiAndTypeResource(imei.toString(),
						TypeResource.ANDROID);
			} catch (ServiceException e) {
				this.LOGGER.info("" + e);
			}

		}
		return this.listResourceAndroid;
	}

	public void selected() {
		mostrarMensajeInformacion("Fichero seleccionado:", resourceSelected.getName());

	}

	// private String synchronizeFile;

	@PostConstruct
	public void init() throws ServiceException {
		// this.synchronizeFile = "/storage/emulated/0/WhatsApp/Media/WhatsApp
		// Images/IMG-20160108-WA0003.jpeg";

		root = new DefaultTreeNode("ho", null);

	}

	public void deleteAllResourceWeb() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listResourceWeb = this.service.findAllResourceByImeiAndTypeResource(imei.toString(),
						TypeResource.WEB);
				for (ResourceDTO resource : this.listResourceWeb) {
					File file = new File(resource.getPathJava());
					if (file.exists() && file.isFile()) {
						if (!file.delete())
							this.mostrarMensajeError("Error al eliminar fichero.", "");
					}
				}

				this.service.deleteAllResourceByImeiAndTypeResource(imei.toString(), TypeResource.WEB);
			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllResourceWeb -> ResourceManagedBean, EXCEPCION: " + e.toString());
			}
		}
		this.mostrarMensajeAlerta("Eliminando.", "");
	}

	public void deleteAllResourceAndroid() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.service.deleteAllResourceByImeiAndTypeResource(imei.toString(), TypeResource.ANDROID);
			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllResourceAndroid -> ResourceManagedBean, EXCEPCION: " + e.toString());
			}
		}
		this.mostrarMensajeAlerta("Eliminando...", "");
	}

	public void synchronize() {
		this.LOGGER.info("Sincronizando rutas de recursos con el dispositivo movil...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				this.service.sendCmd(Protocol.READ_RESOURCE, ip.toString());

				Thread.sleep(Protocol.TIME_SLEEP);
			} catch (InterruptedException e) {
				this.LOGGER.info("ERROR: Sleep durante la sincronizacion ");

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		this.mostrarMensajeInformacion("Sincronizando...", "");
	}

	public StreamedContent getFileDownload(ResourceDTO resource) {
		StreamedContent fileDownload = null;
		this.LOGGER.info("Descargando fichero.");
		try {
			File file = new File(resource.getPathJava());
			InputStream stream = new FileInputStream(file);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			fileDownload = new DefaultStreamedContent(stream, externalContext.getMimeType(file.getName()),
					file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileDownload;
	}

	@SuppressWarnings("resource")
	public void download() {
		this.LOGGER.info("Sincronizando fichero seleccionado con el dispositivo movil....");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				if (this.resourceSelected != null) {
					this.service.sendCmd(Protocol.TAKE_RESOURCE, ip.toString());

					DatagramSocket ds = new DatagramSocket();

					byte[] phoneNumber = this.resourceSelected.getPathAndroid().getBytes();
					DatagramPacket dp1 = new DatagramPacket(phoneNumber, phoneNumber.length,
							InetAddress.getByName(ip.toString()), Protocol.PUERTO);
					ds.send(dp1);

					Thread.sleep(Protocol.TIME_SLEEP);

					this.mostrarMensajeInformacion("Guardando...", "");

					this.mostrarMensajeAlerta("Consulte:", " Recursos Sincronizados.");
				} else
					this.mostrarMensajeAlerta("Por favor...", "Seleccione un fichero.");

			} catch (InterruptedException e) {
				this.LOGGER.info("ERROR: Sleep durante la sincronizacion ");
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			this.mostrarMensajeError("ERROR: ", "Dispositivo seleccionado.");

	}

	public ResourceDTO getResourceSelected() {
		return resourceSelected;
	}

	public void setResourceSelected(ResourceDTO resourceSelected) {
		this.resourceSelected = resourceSelected;
	}

	public void setListResourceWeb(List<ResourceDTO> listResourceWeb) {
		this.listResourceWeb = listResourceWeb;
	}

	public void setListResourceAndroid(List<ResourceDTO> listResourceAndroid) {
		this.listResourceAndroid = listResourceAndroid;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		this.mostrarMensajeInformacion("Seleccionado", event.getTreeNode().toString());
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode getRoot() {

		List<ResourceDTO> list = null;

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				list = this.service.findAllByImei(imei.toString(), Protocol.TYPE_RESOURCEDTO);
			} catch (ServiceException e) {
				System.out.println("error");
			}
			// Ruta raiz (la 0 esta vacia)
			this.root = new DefaultTreeNode(list.get(0).getPathAndroid().split("/")[1], null);
			int cont = 0;
			// System.out.println("Añadiendo rutas: "+list.size());
			Collections.reverse(list);

			for (ResourceDTO f : list) {
				// System.out.println("URL; " + f.getPath());
				String[] token = f.getPathAndroid().split("/");

				int i = 0;
				for (String c : token) {
					// Evitar el primer token del path que es blanco (i a partir
					// de 1 y token != "")

					// Evitar el elemento raiz (i a partir de 2) ya que este no
					// tiene padre
					if (!isCreated(c) && !c.equals(new String("")) && i > 1) {

						// System.out.println("Comrpobado nodo: " + token[i - 1]
						// + "d " + c + " d " + token[i]);

						// Si es carpeta
						if (typeDocument(c) == null) {
							// System.out.println("NO " + c);
							// System.out.println("HIJO: "+c+ " Padre: "+
							// token[i-1]+" PadreNodo
							// "+colgarDe(token[i-1]).getData());
							new DefaultTreeNode(c, colgarDe(token[i - 1]));

						}

						else {// Si es un fichero (audio, video, imagen..)
								// System.out.println("SI " + c);
								// System.out.println("HIJO: "+c+ "Padre: "+
								// token[i-1]+"
								// PadreNodo"+colgarDe(token[i-1]).getData());

							new DefaultTreeNode(typeDocument(c), c, colgarDe(token[i - 1]));
						}

					}

					i++;

				}

				cont++;
				System.out.println("Contado ++ " + cont);
				if (cont == 10)
					break;

			}

		}

		return this.root;
	}

	private String textFilter;

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}

	public List<String> completeFilter(String query) {
		List<String> results = new ArrayList<String>();
		/*
		 * for (String s : getFolderBase()) {
		 * System.out.println("Comproabad22o");
		 * 
		 * if (s.indexOf(query)!=1) results.add(s); }
		 */
		results.add("hola");
		return results;
	}

	List<String> getFolderBase() {
		List<String> listFolderBase = new ArrayList<String>();
		List<ResourceDTO> list = null;

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				list = this.service.findAllByImei(imei.toString(), Protocol.TYPE_RESOURCEDTO);
			} catch (ServiceException e) {
				System.out.println("error");
			}

			String[] token1 = list.get(0).getPathAndroid().split("/");
			int i = 0;
			for (ResourceDTO f : list) {
				if (i > 0) {
					String[] token = f.getPathAndroid().split("/");
					for (int j = 0; j < token.length; j++) {
						if (token[j] != token1[j])
							listFolderBase.add(token[j]);

					}
				}
				i++;

			}
		}
		return listFolderBase;
	}

	String typeDocument(String c) {
		// System.out.println("Comparando: " + c + " " + c.length() + " " +
		// c.lastIndexOf(".") + 1 + " "
		// + (c.length() - c.lastIndexOf(".") + 1));

		// Comprobacion para ver si es extension archivo (aaa.bb, aaa.bbb,
		// aaa.bbb, lleva un solo punto, fichero oculto)
		int lastPoint = c.lastIndexOf(".") + 1;
		int masDeUnPunto = c.substring(c.indexOf(".") + ".".length(), c.length()).indexOf(".");
		boolean hidden = c.indexOf(".") == 0 ? true : false;
		if (((c.length() - lastPoint == 2 || c.length() - lastPoint == 3 || c.length() - lastPoint == 4)
				&& c.indexOf(".") != -1 && masDeUnPunto == -1) || hidden) {

			if (c.indexOf(".flac") != -1 || c.indexOf(".mp3") != -1 || c.indexOf(".wav") != -1
					|| c.indexOf(".aac") != -1 || c.indexOf(".cda") != -1 || c.indexOf(".ogg") != -1)
				return "audio";
			else if (c.indexOf(".mp4") != -1)
				return "video";
			else if (c.indexOf(".zip") != -1)
				return "compressed";
			else if (c.indexOf(".jpg") != -1 || c.indexOf(".png") != -1 || c.indexOf(".jpeg") != -1
					|| c.indexOf(".gif") != -1 || c.indexOf(".bmp") != -1)
				return "image";
			else if (c.indexOf(".") == 0)
				return "hidden";
			else if (c.indexOf(".") != -1)
				return "document";
		}
		return null;

	}

	private TreeNode lastNode;

	TreeNode colgarDe(String token) {
		lastNode = null;
		System.out.println(" INICIO HIJOOOOOOOOOOOOOOO: " + token);
		if (token != null && token != "")
			ultimo(root, token);
		System.out.println("FIN HIJOOOOOOOOOOOOOOO: " + token + " " + lastNode.getData());

		return lastNode;
	}

	public void ultimo(TreeNode rec, String token) {
		List<TreeNode> directorios = rec.getChildren();
		System.out.println("Hijos");
		if (directorios != null && directorios.size() > 0 && this.lastNode == null) {
			// System.out.println("hijo " + directorios.size());
			for (TreeNode t : directorios) {
				System.out.println("comparando token: " + token + " data:" + t.getData());
				if (t.getData().equals(token))
					lastNode = t;
				else
					ultimo(t, token);

			}
		} else {
			if (root.getData().equals(token))
				lastNode = root;// Si no se ha creado ni
			// System.out.println("no hijo");

			// lastNode = rec;

		}

	}

	private boolean created;

	boolean isCreated(String c) {
		created = false;
		// Comprobamos si el elemento raiz es el que quiero volver a crear
		// Luego se hace comparaciones a partir de los hijos
		if (root.getData().equals(c))
			created = true;
		else
			search(root, c);

		return created;
	}

	public void search(TreeNode treeNode, String token) {
		List<TreeNode> directorios = treeNode.getChildren();
		if (directorios != null && !created) {
			for (TreeNode t : directorios) {
				if (t.getData().equals(token))
					created = true;
				else
					search(t, token);

			}
		}

	}

	public List<ResourceDTO> getListFilteredAndroid() {
		return listFilteredAndroid;
	}

	public void setListFilteredAndroid(List<ResourceDTO> listFilteredAndroid) {
		this.listFilteredAndroid = listFilteredAndroid;
	}
	
	

}
