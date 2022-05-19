package com.itemis.gef.tutorial.mindmap.ui.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.mvc.fx.ui.MvcFxUiModule;
import org.eclipse.gef.mvc.fx.ui.parts.AbstractFXEditor;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import com.google.inject.Guice;
import com.google.inject.util.Modules;
import com.itemis.gef.tutorial.mindmap.SimpleMindMapModule;
import com.itemis.gef.tutorial.mindmap.model.AbstractMindMapItem;
import com.itemis.gef.tutorial.mindmap.model.MindMapNode;
import com.itemis.gef.tutorial.mindmap.model.SimpleMindMap;
import com.itemis.gef.tutorial.mindmap.model.SimpleMindMapExampleFactory;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel.Type;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapNodeVisual;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * An Eclipse editor usable as extension.
 * 
 */
public class SimpleMindMapEditor extends AbstractFXEditor {

	public SimpleMindMapEditor() {
		super(Guice.createInjector(Modules.override(new SimpleMindMapModule()).with(new MvcFxUiModule())));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// получаем содержимое средства просмотра
		SimpleMindMap mindmap = (SimpleMindMap) getContentViewer().getContents().get(0);
		try { // сериализовать карту разума
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(mindmap);
			oos.close();
			// запись в файл
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
			markNonDirty();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		SimpleMindMapExampleFactory fac = new SimpleMindMapExampleFactory();

        SimpleMindMap mindmap = fac.createComplexExample();
		//SimpleMindMap mindmap = null; // прочитать данный входной файл
//		try {
//			IFile file = ((IFileEditorInput) input).getFile();
//			ObjectInputStream is = new ObjectInputStream(file.getContents());
//			mindmap = (SimpleMindMap) is.readObject();
//			is.close();
//			setPartName(file.getName());
//
//			// сброс цвета по умолчанию, так как мы не сохранили цвет
//			for (AbstractMindMapItem item : mindmap.getChildElements()) {
//				if (item instanceof MindMapNode) {
//					((MindMapNode) item).setColor(Color.GREENYELLOW);
//				}
//			}
//
//		} catch (EOFException e) {
//			// создать новую карту SimpleMindMap...
//			mindmap = new SimpleMindMap();
//		} catch (Exception e) {
//			throw new PartInitException("Could not load input", e);
//		}
		getContentViewer().getContents().setAll(Collections.singletonList(mindmap));
		
	}

	/**
	 * Creating JavaFX widgets and set them to the stage.
	 */
	@Override
	protected void hookViewers() {
		final IViewer contentViewer = getContentViewer();

		// создание родительской панели для холста и палитры
		HBox pane = new HBox();

		pane.getChildren().add(contentViewer.getCanvas());
		pane.getChildren().add(createToolPalette());
		HBox.setHgrow(contentViewer.getCanvas(), Priority.ALWAYS);

		Scene scene = new Scene(pane);
		getCanvas().setScene(scene);
	}

	private Node createToolPalette() {
		ItemCreationModel creationModel = getContentViewer().getAdapter(ItemCreationModel.class);

		MindMapNodeVisual graphic = new MindMapNodeVisual();
		graphic.setTitle("New Node");

		// ToggleGroup гарантирует, что мы выбираем только одну
		ToggleGroup toggleGroup = new ToggleGroup();

		ToggleButton createNode = new ToggleButton("", graphic);
    	createNode.setToggleGroup(toggleGroup);
    	createNode.setMaxWidth(Double.MAX_VALUE);
    	createNode.selectedProperty().addListener((e, oldVal, newVal) -> {
    		creationModel.setType(newVal ? Type.Node : Type.None);
    	});

    	ToggleButton createConn = new ToggleButton("New Connection");
    	createConn.setToggleGroup(toggleGroup);
    	createConn.setMaxWidth(Double.MAX_VALUE);
    	createConn.setMinHeight(50);
    	createConn.selectedProperty().addListener((e, oldVal, newVal) -> {
                    creationModel.setType(newVal ? Type.Connection : Type.None);
    	});

		// теперь слушаем изменения в модели и деактивируем кнопки, если
		// нужно
		 // теперь прослушиваем изменения в модели и отключаем кнопки, если это  необходимо 
        creationModel.getTypeProperty().addListener((e, oldVal, newVal) -> {
            if (Type.None == newVal) {
                // unselect the toggle button
                Toggle selectedToggle = toggleGroup.getSelectedToggle();
                if (selectedToggle != null) {
                    selectedToggle.setSelected(false);
                }
            }
        });  

		return new VBox(20, createNode, createConn);
	}
}