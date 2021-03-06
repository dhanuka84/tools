/**
 */
package dataMapper;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link dataMapper.Output#getTreeNode <em>Tree Node</em>}</li>
 * </ul>
 * </p>
 *
 * @see dataMapper.DataMapperPackage#getOutput()
 * @model
 * @generated
 */
public interface Output extends DataMapperNode {

	/**
	 * Returns the value of the '<em><b>Tree Node</b></em>' containment reference list.
	 * The list contents are of type {@link dataMapper.TreeNode}.
	 * It is bidirectional and its opposite is '{@link dataMapper.TreeNode#getOutputParent <em>Output Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tree Node</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tree Node</em>' containment reference list.
	 * @see dataMapper.DataMapperPackage#getOutput_TreeNode()
	 * @see dataMapper.TreeNode#getOutputParent
	 * @model opposite="outputParent" containment="true"
	 * @generated
	 */
	EList<TreeNode> getTreeNode();
} // Output
