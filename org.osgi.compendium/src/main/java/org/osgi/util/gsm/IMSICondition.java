/*
 * Copyright (c) OSGi Alliance (2004, 2009). All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osgi.util.gsm;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.osgi.framework.Bundle;
import org.osgi.service.condpermadmin.Condition;
import org.osgi.service.condpermadmin.ConditionInfo;

/**
 * Class representing an IMSI condition. Instances of this class contain a
 * string value that is matched against the IMSI of the subscriber.
 * 
 * @ThreadSafe
 * @version $Revision: 6439 $
 */
public class IMSICondition {
	private static final String	ORG_OSGI_UTIL_GSM_IMSI	= "org.osgi.util.gsm.imsi";
	private static final String	IMSI;
	private static final int	IMSI_LENGTH				= 15;

	static {
		IMSI = (String) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return System.getProperty(ORG_OSGI_UTIL_GSM_IMSI);
			}
		});
	}

	private IMSICondition() {
		// prevent instances being constructed
	}

	/**
	 * Creates an IMSI condition object.
	 * 
	 * @param bundle This parameter is ignored, as the IMSI number is a property
	 *        of the mobile subscriber and thus is the same for all bundles.
	 * @param conditionInfo Contains the IMSI value against which to match the
	 *        subscriber's IMSI. Its {@link ConditionInfo#getArgs()} method
	 *        should return a String array with one value: the IMSI string. The
	 *        IMSI is 15 digits without hyphens. Limited pattern matching is
	 *        allowed: the string is 0 to 14 digits, followed by an asterisk (
	 *        <code>*</code>).
	 * @return A Condition object that indicates whether the specified IMSI
	 *         number matches that of the subscriber. If the number ends with an
	 *         asterisk (<code>*</code>), then the beginning of the IMSI is
	 *         compared to the pattern.
	 * @throws IllegalArgumentException If the IMSI is not a string of 15
	 *         digits, or 0 to 14 digits with an <code>*</code> at the end.
	 */
	public static Condition getCondition(Bundle bundle,
			ConditionInfo conditionInfo) {
		String imsi = conditionInfo.getArgs()[0];
		int length = imsi.length();
		if (length > IMSI_LENGTH) {
			throw new IllegalArgumentException("IMSI too long: " + imsi);
		}
		if (imsi.endsWith("*")) {
			length--;
			imsi = imsi.substring(0, length);
		}
		else {
			if (length < IMSI_LENGTH) {
				throw new IllegalArgumentException("IMSI too short: " + imsi);
			}
		}
		for (int i = 0; i < length; i++) {
			char c = imsi.charAt(i);
			if (('0' <= c) && (c <= '9')) {
				continue;
			}
			throw new IllegalArgumentException("not a valid IMSI: " + imsi);
		}
		if (IMSI == null) {
			System.err
					.println("The OSGi implementation of org.osgi.util.gsm.IMSICondition needs the system property "
							+ ORG_OSGI_UTIL_GSM_IMSI + " set.");
			return Condition.FALSE;
		}
		return IMSI.startsWith(imsi) ? Condition.TRUE : Condition.FALSE;
	}
}
