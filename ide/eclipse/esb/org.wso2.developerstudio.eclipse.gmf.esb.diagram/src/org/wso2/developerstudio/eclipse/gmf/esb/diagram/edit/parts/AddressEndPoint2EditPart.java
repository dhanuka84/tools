/*
 * Copyright WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.wso2.developerstudio.eclipse.gmf.esb.AddressEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.FailoverEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.LoadBalanceEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.SendMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.EsbGraphicalShape;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.FixedBorderItemLocator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.ShowPropertyViewEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.editpolicy.FeedbackIndicateDragDropEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.AddressEndPoint2CanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.AddressEndPoint2ItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbVisualIDRegistry;

/**
 * @generated 
 */
public class AddressEndPoint2EditPart extends AbstractBorderedShapeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3646;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public AddressEndPoint2EditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new AddressEndPoint2ItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new FeedbackIndicateDragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new AddressEndPoint2CanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// For handle Double click Event.
		installEditPolicy(EditPolicyRoles.OPEN_ROLE, new ShowPropertyViewEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
        removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				View childView = (View) child.getModel();
				switch (EsbVisualIDRegistry.getVisualID(childView)) {
				case AddressEndPointInputConnector2EditPart.VISUAL_ID:
				case AddressEndPointOutputConnector2EditPart.VISUAL_ID:
					return new BorderItemSelectionEditPolicy();
				}
				EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated NOT
	 */
	protected IFigure createNodeShape() {
		return primaryShape = new AddressEndPointFigure();
	}

	/**
	 * @generated
	 */
	public AddressEndPointFigure getPrimaryShape() {
		return (AddressEndPointFigure) primaryShape;
	}

	/**
	 * @generated NOT
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof AddressEndPointEndPointName2EditPart) {
			((AddressEndPointEndPointName2EditPart) childEditPart).setLabel(getPrimaryShape()
					.getFigureAddressEndPointNamePropertyLabel());
			return true;
		}
		if (childEditPart instanceof AddressEndPointInputConnector2EditPart) {
			double position;
			EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (childEditPart
					.getParent()).getModel()).getElement();
			if (((AddressEndPoint) parentEndpoint).getInputConnector().getIncomingLinks().size() != 0) {
				EObject source = ((AddressEndPoint) parentEndpoint).getInputConnector()
						.getIncomingLinks().get(0).getSource().eContainer();
				position = ((source instanceof LoadBalanceEndPoint)
						|| (source instanceof FailoverEndPoint) || (source instanceof SendMediator)) ? 0.5
						: 0.25;
			} else {
				position = 0.25;
			}
			IFigure borderItemFigure = ((AddressEndPointInputConnector2EditPart) childEditPart)
					.getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(getMainFigure(),
					borderItemFigure, PositionConstants.WEST, position);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
			return true;
		}
		if (childEditPart instanceof AddressEndPointOutputConnector2EditPart) {
			IFigure borderItemFigure = ((AddressEndPointOutputConnector2EditPart) childEditPart)
					.getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(getMainFigure(),
					borderItemFigure, PositionConstants.WEST, 0.75);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof AddressEndPointEndPointName2EditPart) {
			return true;
		}
		if (childEditPart instanceof AddressEndPointInputConnector2EditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((AddressEndPointInputConnector2EditPart) childEditPart).getFigure());
			return true;
		}
		if (childEditPart instanceof AddressEndPointOutputConnector2EditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((AddressEndPointOutputConnector2EditPart) childEditPart).getFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof IBorderItemEditPart) {
			return getBorderedFigure().getBorderItemContainer();
		}
		return getContentPane();
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createMainFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(5);
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setBackgroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setBackgroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(EsbVisualIDRegistry
				.getType(AddressEndPointEndPointName2EditPart.VISUAL_ID));
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		AddressEndPoint addEp = (AddressEndPoint) resolveSemanticElement();

		if (addEp != null) {
			if (addEp.getURI() != null) {
				getPrimaryShape().setToolTip(new Label(addEp.getURI()));
			}

		}
	}

	protected void handleNotificationEvent(Notification notification) {
		super.handleNotificationEvent(notification);
		if (notification.getNotifier() instanceof AddressEndPoint) {
			refreshVisuals();
		}
	}

	/**
	 * @generated
	 */
	public class AddressEndPointFigure extends EsbGraphicalShape {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureAddressEndPointNamePropertyLabel;

		/**
		 * @generated
		 */
		public AddressEndPointFigure() {
			this.setBackgroundColor(THIS_BACK);
			createContents();
		}

		/**
		 * @generated NOT
		 */
		private void createContents() {

			fFigureAddressEndPointNamePropertyLabel = new WrappingLabel();
			fFigureAddressEndPointNamePropertyLabel.setText("<...>");
			fFigureAddressEndPointNamePropertyLabel.setAlignment(SWT.CENTER);

			this.getPropertyValueRectangle1().add(fFigureAddressEndPointNamePropertyLabel);

		}

		public String getIconPath() {
			return "icons/ico20/address-endpoint.gif";
		}

		public String getNodeName() {
			return "Add-EP";
		}

		public Color getBackgroundColor() {
			return THIS_BACK;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureAddressEndPointNamePropertyLabel() {
			return fFigureAddressEndPointNamePropertyLabel;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 248, 151, 40);

}/*package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.wso2.developerstudio.eclipse.gmf.esb.AddressEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.DefaultEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.FailoverEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.LoadBalanceEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractEndpoint;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.EsbGraphicalShape;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.FixedBorderItemLocator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.AddressEndPoint2CanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.AddressEndPoint2ItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbVisualIDRegistry;

 */
/**
 * @generated NOT
 */
/*
 public class AddressEndPoint2EditPart extends AbstractEndpoint {

 *//**
 * @generated
 */
/*
 public static final int VISUAL_ID = 3383;

 *//**
 * @generated
 */
/*
 protected IFigure contentPane;

 *//**
 * @generated
 */
/*
 protected IFigure primaryShape;

 *//**
 * @generated
 */
/*
 public AddressEndPoint2EditPart(View view) {
 super(view);
 }

 *//**
 * @generated
 */
/*
 protected void createDefaultEditPolicies() {
 installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
 super.createDefaultEditPolicies();
 installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
 new AddressEndPoint2ItemSemanticEditPolicy());
 installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
 installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new AddressEndPoint2CanonicalEditPolicy());
 installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
 // XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
 // removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
 }

 *//**
 * @generated
 */
/*
 protected LayoutEditPolicy createLayoutEditPolicy() {
 org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep =
 new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

 protected EditPolicy createChildEditPolicy(EditPart child) {
 View childView =
 (View) child.getModel();
 switch (EsbVisualIDRegistry.getVisualID(childView)) {
 case AddressEndPointInputConnectorEditPart.VISUAL_ID:
 case AddressEndPointOutputConnectorEditPart.VISUAL_ID:
 return new BorderItemSelectionEditPolicy();
 }
 EditPolicy result =
 child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
 if (result == null) {
 result =
 new NonResizableEditPolicy();
 }
 return result;
 }

 protected Command getMoveChildrenCommand(Request request) {
 return null;
 }

 protected Command getCreateCommand(CreateRequest request) {
 return null;
 }
 };
 return lep;
 }

 *//**
 * @generated
 */
/*
 protected IFigure createNodeShape() {
 return primaryShape = new AddressEndPointFigure();
 }

 *//**
 * @generated
 */
/*
 public AddressEndPointFigure getPrimaryShape() {
 return (AddressEndPointFigure) primaryShape;
 }

 *//**
 * @generated NOT
 */
/*
 protected boolean addFixedChild(EditPart childEditPart) {
 if (childEditPart instanceof AddressEndPointEndPointName2EditPart) {
 ((AddressEndPointEndPointName2EditPart) childEditPart).setLabel(getPrimaryShape().getFigureAddressEndPointNamePropertyLabel());
 return true;
 }
 if (childEditPart instanceof AddressEndPointInputConnectorEditPart) {
 double position;
 EObject parentEndpoint =
 ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (childEditPart.getParent()).getModel()).getElement();
 if (((AddressEndPoint) parentEndpoint).getInputConnector().getIncomingLinks().size() != 0) {
 EObject source =
 ((AddressEndPoint) parentEndpoint).getInputConnector()
 .getIncomingLinks().get(0)
 .getSource().eContainer();
 position =
 ((source instanceof LoadBalanceEndPoint) || (source instanceof FailoverEndPoint))
 ? 0.5
 : 0.25;
 } else {
 position = 0.25;
 }
 IFigure borderItemFigure =
 ((AddressEndPointInputConnectorEditPart) childEditPart).getFigure();
 BorderItemLocator locator =
 new FixedBorderItemLocator(getMainFigure(),
 borderItemFigure,
 PositionConstants.WEST, position);
 getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
 return true;
 }
 if (childEditPart instanceof AddressEndPointOutputConnectorEditPart) {
 IFigure borderItemFigure =
 ((AddressEndPointOutputConnectorEditPart) childEditPart).getFigure();
 BorderItemLocator locator =
 new FixedBorderItemLocator(getMainFigure(),
 borderItemFigure,
 PositionConstants.WEST, 0.75);
 getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
 return true;
 }
 return false;
 }

 *//**
 * @generated
 */
/*
 protected boolean removeFixedChild(EditPart childEditPart) {
 if (childEditPart instanceof AddressEndPointEndPointName2EditPart) {
 return true;
 }
 if (childEditPart instanceof AddressEndPointInputConnectorEditPart) {
 getBorderedFigure().getBorderItemContainer()
 .remove(((AddressEndPointInputConnectorEditPart) childEditPart).getFigure());
 return true;
 }
 if (childEditPart instanceof AddressEndPointOutputConnectorEditPart) {
 getBorderedFigure().getBorderItemContainer()
 .remove(((AddressEndPointOutputConnectorEditPart) childEditPart).getFigure());
 return true;
 }
 return false;
 }

 *//**
 * @generated
 */
/*
 protected void addChildVisual(EditPart childEditPart, int index) {
 if (addFixedChild(childEditPart)) {
 return;
 }
 super.addChildVisual(childEditPart, -1);
 }

 *//**
 * @generated
 */
/*
 protected void removeChildVisual(EditPart childEditPart) {
 if (removeFixedChild(childEditPart)) {
 return;
 }
 super.removeChildVisual(childEditPart);
 }

 *//**
 * @generated
 */
/*
 protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
 if (editPart instanceof IBorderItemEditPart) {
 return getBorderedFigure().getBorderItemContainer();
 }
 return getContentPane();
 }

 *//**
 * @generated
 */
/*
 protected NodeFigure createNodePlate() {
 DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
 return result;
 }

 *//**
 * Creates figure for this edit part.
 * 
 * Body of this method does not depend on settings in generation model
 * so you may safely remove <i>generated</i> tag and modify it.
 * 
 * @generated
 */
/*
 protected NodeFigure createMainFigure() {
 NodeFigure figure = createNodePlate();
 figure.setLayoutManager(new StackLayout());
 IFigure shape = createNodeShape();
 figure.add(shape);
 contentPane = setupContentPane(shape);
 return figure;
 }

 *//**
 * Default implementation treats passed figure as content pane.
 * Respects layout one may have set for generated figure.
 * @param nodeShape instance of generated figure class
 * @generated
 */
/*
 protected IFigure setupContentPane(IFigure nodeShape) {
 if (nodeShape.getLayoutManager() == null) {
 ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
 layout.setSpacing(5);
 nodeShape.setLayoutManager(layout);
 }
 return nodeShape; // use nodeShape itself as contentPane
 }

 *//**
 * @generated
 */
/*
 public IFigure getContentPane() {
 if (contentPane != null) {
 return contentPane;
 }
 return super.getContentPane();
 }

 *//**
 * @generated
 */
/*
 protected void setForegroundColor(Color color) {
 if (primaryShape != null) {
 primaryShape.setForegroundColor(color);
 }
 }

 *//**
 * @generated
 */
/*
 protected void setBackgroundColor(Color color) {
 if (primaryShape != null) {
 primaryShape.setBackgroundColor(color);
 }
 }

 *//**
 * @generated
 */
/*
 protected void setLineWidth(int width) {
 if (primaryShape instanceof Shape) {
 ((Shape) primaryShape).setLineWidth(width);
 }
 }

 *//**
 * @generated
 */
/*
 protected void setLineType(int style) {
 if (primaryShape instanceof Shape) {
 ((Shape) primaryShape).setLineStyle(style);
 }
 }

 *//**
 * @generated
 */
/*
 public EditPart getPrimaryChildEditPart() {
 return getChildBySemanticHint(EsbVisualIDRegistry.getType(AddressEndPointEndPointName2EditPart.VISUAL_ID));
 }

 *//**
 * @generated
 */
/*
 public class AddressEndPointFigure extends EsbGraphicalShape {

 *//**
 * @generated
 */
/*
 private WrappingLabel fFigureAddressEndPointNamePropertyLabel;

 *//**
 * @generated
 */
/*
 public AddressEndPointFigure() {

 this.setBackgroundColor(THIS_BACK);
 createContents();
 }

 *//**
 * @generated NOT
 */
/*
 private void createContents() {

 fFigureAddressEndPointNamePropertyLabel = new WrappingLabel();
 fFigureAddressEndPointNamePropertyLabel.setText("<...>");

 fFigureAddressEndPointNamePropertyLabel.setAlignment(SWT.CENTER);

 this.getPropertyValueRectangle1().add(fFigureAddressEndPointNamePropertyLabel);

 }

 *//**
 * @generated
 */
/*
 public WrappingLabel getFigureAddressEndPointNamePropertyLabel() {
 return fFigureAddressEndPointNamePropertyLabel;
 }

 public String getIconPath() {
 return "icons/ico20/address-endpoint.gif";
 }

 public String getNodeName() {
 return "Add-EP";
 }

 public Color getBackgroundColor() {
 return THIS_BACK;
 }

 public Color getLabelBackColor() {
 return THIS_LABEL_BACK;
 }

 }

 *//**
 * @generated
 */
/*
 static final Color THIS_BACK = new Color(null, 248, 151, 40);

 static final Color THIS_LABEL_BACK = new Color(null, 255, 225, 194);

 }
 */