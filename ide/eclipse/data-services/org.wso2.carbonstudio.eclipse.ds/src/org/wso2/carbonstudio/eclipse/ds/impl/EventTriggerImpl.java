/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.wso2.carbonstudio.eclipse.ds.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.wso2.carbonstudio.eclipse.ds.DsPackage;
import org.wso2.carbonstudio.eclipse.ds.EventSubscriptionList;
import org.wso2.carbonstudio.eclipse.ds.EventTrigger;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.wso2.carbonstudio.eclipse.ds.impl.EventTriggerImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.wso2.carbonstudio.eclipse.ds.impl.EventTriggerImpl#getTargetTopic <em>Target Topic</em>}</li>
 *   <li>{@link org.wso2.carbonstudio.eclipse.ds.impl.EventTriggerImpl#getSubscriptions <em>Subscriptions</em>}</li>
 *   <li>{@link org.wso2.carbonstudio.eclipse.ds.impl.EventTriggerImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.wso2.carbonstudio.eclipse.ds.impl.EventTriggerImpl#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EventTriggerImpl extends EObjectImpl implements EventTrigger {
	/**
	 * The default value of the '{@link #getExpression() <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExpression() <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected String expression = EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getTargetTopic() <em>Target Topic</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetTopic()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_TOPIC_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetTopic() <em>Target Topic</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetTopic()
	 * @generated
	 * @ordered
	 */
	protected String targetTopic = TARGET_TOPIC_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSubscriptions() <em>Subscriptions</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubscriptions()
	 * @generated
	 * @ordered
	 */
	protected EventSubscriptionList subscriptions;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
	protected static final String LANGUAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
	protected String language = LANGUAGE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventTriggerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DsPackage.Literals.EVENT_TRIGGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpression(String newExpression) {
		String oldExpression = expression;
		expression = newExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DsPackage.EVENT_TRIGGER__EXPRESSION, oldExpression, expression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTargetTopic() {
		return targetTopic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetTopic(String newTargetTopic) {
		String oldTargetTopic = targetTopic;
		targetTopic = newTargetTopic;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DsPackage.EVENT_TRIGGER__TARGET_TOPIC, oldTargetTopic, targetTopic));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventSubscriptionList getSubscriptions() {
		return subscriptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSubscriptions(EventSubscriptionList newSubscriptions, NotificationChain msgs) {
		EventSubscriptionList oldSubscriptions = subscriptions;
		subscriptions = newSubscriptions;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS, oldSubscriptions, newSubscriptions);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubscriptions(EventSubscriptionList newSubscriptions) {
		if (newSubscriptions != subscriptions) {
			NotificationChain msgs = null;
			if (subscriptions != null)
				msgs = ((InternalEObject)subscriptions).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS, null, msgs);
			if (newSubscriptions != null)
				msgs = ((InternalEObject)newSubscriptions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS, null, msgs);
			msgs = basicSetSubscriptions(newSubscriptions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS, newSubscriptions, newSubscriptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DsPackage.EVENT_TRIGGER__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLanguage(String newLanguage) {
		String oldLanguage = language;
		language = newLanguage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DsPackage.EVENT_TRIGGER__LANGUAGE, oldLanguage, language));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS:
				return basicSetSubscriptions(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DsPackage.EVENT_TRIGGER__EXPRESSION:
				return getExpression();
			case DsPackage.EVENT_TRIGGER__TARGET_TOPIC:
				return getTargetTopic();
			case DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS:
				return getSubscriptions();
			case DsPackage.EVENT_TRIGGER__ID:
				return getId();
			case DsPackage.EVENT_TRIGGER__LANGUAGE:
				return getLanguage();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DsPackage.EVENT_TRIGGER__EXPRESSION:
				setExpression((String)newValue);
				return;
			case DsPackage.EVENT_TRIGGER__TARGET_TOPIC:
				setTargetTopic((String)newValue);
				return;
			case DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS:
				setSubscriptions((EventSubscriptionList)newValue);
				return;
			case DsPackage.EVENT_TRIGGER__ID:
				setId((String)newValue);
				return;
			case DsPackage.EVENT_TRIGGER__LANGUAGE:
				setLanguage((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DsPackage.EVENT_TRIGGER__EXPRESSION:
				setExpression(EXPRESSION_EDEFAULT);
				return;
			case DsPackage.EVENT_TRIGGER__TARGET_TOPIC:
				setTargetTopic(TARGET_TOPIC_EDEFAULT);
				return;
			case DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS:
				setSubscriptions((EventSubscriptionList)null);
				return;
			case DsPackage.EVENT_TRIGGER__ID:
				setId(ID_EDEFAULT);
				return;
			case DsPackage.EVENT_TRIGGER__LANGUAGE:
				setLanguage(LANGUAGE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DsPackage.EVENT_TRIGGER__EXPRESSION:
				return EXPRESSION_EDEFAULT == null ? expression != null : !EXPRESSION_EDEFAULT.equals(expression);
			case DsPackage.EVENT_TRIGGER__TARGET_TOPIC:
				return TARGET_TOPIC_EDEFAULT == null ? targetTopic != null : !TARGET_TOPIC_EDEFAULT.equals(targetTopic);
			case DsPackage.EVENT_TRIGGER__SUBSCRIPTIONS:
				return subscriptions != null;
			case DsPackage.EVENT_TRIGGER__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case DsPackage.EVENT_TRIGGER__LANGUAGE:
				return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (expression: ");
		result.append(expression);
		result.append(", targetTopic: ");
		result.append(targetTopic);
		result.append(", id: ");
		result.append(id);
		result.append(", language: ");
		result.append(language);
		result.append(')');
		return result.toString();
	}

} //EventTriggerImpl
