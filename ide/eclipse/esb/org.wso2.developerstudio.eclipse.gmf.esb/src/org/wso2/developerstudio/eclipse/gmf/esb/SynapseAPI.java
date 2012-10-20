/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.wso2.developerstudio.eclipse.gmf.esb;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Synapse API</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getApiName <em>Api Name</em>}</li>
 *   <li>{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getContext <em>Context</em>}</li>
 *   <li>{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getHostName <em>Host Name</em>}</li>
 *   <li>{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getPort <em>Port</em>}</li>
 *   <li>{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getResources <em>Resources</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage#getSynapseAPI()
 * @model
 * @generated
 */
public interface SynapseAPI extends EsbElement {
	/**
	 * Returns the value of the '<em><b>Api Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Api Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Api Name</em>' attribute.
	 * @see #setApiName(String)
	 * @see org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage#getSynapseAPI_ApiName()
	 * @model
	 * @generated
	 */
	String getApiName();

	/**
	 * Sets the value of the '{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getApiName <em>Api Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Api Name</em>' attribute.
	 * @see #getApiName()
	 * @generated
	 */
	void setApiName(String value);

	/**
	 * Returns the value of the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' attribute.
	 * @see #setContext(String)
	 * @see org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage#getSynapseAPI_Context()
	 * @model
	 * @generated
	 */
	String getContext();

	/**
	 * Sets the value of the '{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getContext <em>Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' attribute.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(String value);

	/**
	 * Returns the value of the '<em><b>Host Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Host Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Host Name</em>' attribute.
	 * @see #setHostName(String)
	 * @see org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage#getSynapseAPI_HostName()
	 * @model
	 * @generated
	 */
	String getHostName();

	/**
	 * Sets the value of the '{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getHostName <em>Host Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Host Name</em>' attribute.
	 * @see #getHostName()
	 * @generated
	 */
	void setHostName(String value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' attribute.
	 * @see #setPort(String)
	 * @see org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage#getSynapseAPI_Port()
	 * @model
	 * @generated
	 */
	String getPort();

	/**
	 * Sets the value of the '{@link org.wso2.developerstudio.eclipse.gmf.esb.SynapseAPI#getPort <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' attribute.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(String value);

	/**
	 * Returns the value of the '<em><b>Resources</b></em>' containment reference list.
	 * The list contents are of type {@link org.wso2.developerstudio.eclipse.gmf.esb.APIResource}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resources</em>' containment reference list.
	 * @see org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage#getSynapseAPI_Resources()
	 * @model containment="true"
	 * @generated
	 */
	EList<APIResource> getResources();

} // SynapseAPI