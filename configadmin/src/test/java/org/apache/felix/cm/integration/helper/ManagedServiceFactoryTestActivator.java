/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.cm.integration.helper;


import java.util.Dictionary;

import org.ops4j.pax.swissbox.tinybundles.dp.Constants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedServiceFactory;


public class ManagedServiceFactoryTestActivator extends BaseTestActivator
{

    public static ManagedServiceFactoryTestActivator INSTANCE;

    private Dictionary registrationProps;
    private ServiceRegistration registration;


    public void start( BundleContext context ) throws Exception
    {
        this.registrationProps = getServiceProperties( context );
        this.registration = context.registerService( ManagedServiceFactory.class.getName(), this,
            this.registrationProps );
        INSTANCE = this;
    }


    public void stop( BundleContext arg0 ) throws Exception
    {
        INSTANCE = null;
    }


    public void changePid( final String newPid )
    {
        this.registrationProps.put( Constants.SERVICE_PID, newPid );
        this.registration.setProperties( this.registrationProps );
    }
}
