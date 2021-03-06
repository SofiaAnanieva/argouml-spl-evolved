// $Id: CopyHelperEUMLImpl.java 26 2010-04-03 18:15:09Z marcusvnac $
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

import org.argouml.model.CopyHelper;
import org.eclipse.uml2.uml.Element;

import org.argouml.model.euml.UMLUtil;
import org.argouml.model.euml.EUMLModelImplementation;

/**
 * The implementation of the CopyHelper for EUML2.
 */
class CopyHelperEUMLImpl implements CopyHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *                The ModelImplementation.
     */
    public CopyHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }
    
    public Element copy(Object source, Object destination) {
        if (!(source instanceof Element) || !(destination instanceof Element)) {
            throw new IllegalArgumentException(
                    "The source and destination must be instances of Element"); //$NON-NLS-1$
        }
        Element copiedElement = UMLUtil.copy(
                modelImpl, (Element) source, (Element) destination);
        if (copiedElement == null) {
            throw new UnsupportedOperationException(
                    "Could not copy " + source + " to destination " + destination); //$NON-NLS-1$//$NON-NLS-2$
        }
        return copiedElement;
    }
   

}
