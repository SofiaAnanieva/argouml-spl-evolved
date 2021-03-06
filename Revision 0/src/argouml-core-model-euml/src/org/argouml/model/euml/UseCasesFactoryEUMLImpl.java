// $Id: UseCasesFactoryEUMLImpl.java 122 2010-09-24 00:46:37Z marcusvnac $
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.NotImplementedException;
import org.argouml.model.UseCasesFactory;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UseCase;

import org.argouml.model.euml.EUMLModelImplementation;

/**
 * The implementation of the UseCasesFactory for EUML2.
 */
class UseCasesFactoryEUMLImpl implements UseCasesFactory, AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public UseCasesFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Actor buildActor(Object actor, Object model) {
        if (!(actor instanceof Actor)) {
            throw new IllegalArgumentException();
        }
        if (((Actor) actor).getNamespace() == null
                && !(model instanceof Namespace)) {
            throw new IllegalArgumentException();
        }
        Namespace ns = ((Actor) actor).getNamespace();
        if (ns == null) {
            ns = (Namespace) model;
        }
        Actor ret = createActor();
        modelImpl.getCoreHelper().addOwnedElement(
                ns, ret, "Create the actor # in the namespace #", ret, ns);
//        ret.setIsLeaf(false);
//        ret.setIsRoot(false);
        return ret;
    }

    public Extend buildExtend(Object abase, Object anextension) {
        return buildExtend(abase, anextension, null);
    }

    public Extend buildExtend(final Object extendedCase,
            final Object extension, final Object extensionLocation) {
        if (!(extendedCase instanceof UseCase)
                || !(extension instanceof UseCase)) {
            throw new IllegalArgumentException();
        }
        if (extensionLocation != null
                && !(extensionLocation instanceof ExtensionPoint)) {
            throw new IllegalArgumentException();
        }
        if (extensionLocation != null
                && !((ExtensionPoint) extensionLocation).getUseCase().equals(
                        extendedCase)) {
            throw new IllegalArgumentException(
                    "extensionLocation must belong to " + extendedCase); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ExtensionPoint ep;
                if (extensionLocation == null) {
                    ep = createExtensionPoint();
                    ((UseCase) extendedCase).getExtensionPoints().add(ep);
                } else {
                    ep = (ExtensionPoint) extensionLocation;
                }
                Extend extend = createExtend();
                extend.setExtendedCase((UseCase) extendedCase);
                extend.setExtension((UseCase) extension);
                extend.getExtensionLocations().add(ep);
                getParams().add(extend);
                getParams().add(ep);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the extend # for the case # that extends the case # through #");
        modelImpl.getEditingDomain().getCommandStack().execute(cmd);
        cmd.setObjects(
                run.getParams().get(0), extension, extendedCase,
                run.getParams().get(1));

        return (Extend) run.getParams().get(0);
    }

    public ExtensionPoint buildExtensionPoint(Object modelElement) {
        if (!(modelElement instanceof UseCase)) {
            throw new IllegalArgumentException();
        }
        ExtensionPoint ep = createExtensionPoint();
        modelImpl.getCoreHelper().addOwnedElement(
                modelElement, ep,
                "Create the extension point # for the case #", ep,
                modelElement);
        return ep;
    }

    public Include buildInclude(final Object addition,
            final Object includingCase) {
        if (!(addition instanceof UseCase)
                || !(includingCase instanceof UseCase)) {
            throw new IllegalArgumentException();
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Include include = createInclude();
                include.setAddition((UseCase) addition);
                include.setIncludingCase((UseCase) includingCase);
                getParams().add(include);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the include # of the including case # that include the case #");
        modelImpl.getEditingDomain().getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), includingCase, addition);

        return (Include) run.getParams().get(0);
    }

    public Actor createActor() {
        return UMLFactory.eINSTANCE.createActor();
    }

    public Extend createExtend() {
        return UMLFactory.eINSTANCE.createExtend();
    }

    public ExtensionPoint createExtensionPoint() {
        return UMLFactory.eINSTANCE.createExtensionPoint();
    }

    public Include createInclude() {
        return UMLFactory.eINSTANCE.createInclude();
    }
    
    public UseCase createUseCase() {
        return UMLFactory.eINSTANCE.createUseCase();
    }


}
