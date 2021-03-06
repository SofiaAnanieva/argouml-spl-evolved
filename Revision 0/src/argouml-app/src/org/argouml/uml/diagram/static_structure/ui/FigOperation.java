// $Id: FigOperation.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2006-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

import org.argouml.uml.diagram.static_structure.ui.FigFeature;
import org.argouml.model.ModelEventPump;
import org.argouml.model.Facade;

/**
 * Fig with specific knowledge of Operation and Reception display. 
 * Makes the text italic in case the Operation or Reception is abstract.
 *
 * @since 0.23.5
 * @author Bob Tarling
 */
public class FigOperation extends FigFeature {

    /**
     * Constructor.
     * 
     * @param x x
     * @param y x
     * @param w w
     * @param h h
     * @param aFig the fig
     * @param np the notation provider for the text
     * @deprecated for 0.27.3 by tfmorris.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigOperation(int x, int y, int w, int h, Fig aFig, 
            NotationProvider np) {
        super(x, y, w, h, aFig, np);
    }

    /**
     * Construct a fig for a UML Operation.
     * @deprecated by mvw in V0.27.3. Use the constructor without np parameter.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     * @param np notation provider
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigOperation(Object owner, Rectangle bounds,
            DiagramSettings settings, NotationProvider np) {
        super(owner, bounds, settings, np);
        Model.getPump().addModelEventListener(this, owner, "isAbstract");
    }

    /**
     * Construct a fig for a UML Operation
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigOperation(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        Model.getPump().addModelEventListener(this, owner, "isAbstract");
    }    
    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#setOwner(java.lang.Object)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public void setOwner(Object owner) {
        super.setOwner(owner);

        if (owner != null) {
            diagramFontChanged(null);
            Model.getPump().addModelEventListener(this, owner, "isAbstract");
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        Model.getPump().removeModelEventListener(this, getOwner(), 
                "isAbstract");
        super.removeFromDiagram();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);
        if ("isAbstract".equals(pce.getPropertyName())) {
            renderingChanged();    
        }
    }

    /*
     * If the Operation/Reception is abstract, 
     * then the text will be set to italics.
     */
    @Override
    protected int getFigFontStyle() {
        return Model.getFacade().isAbstract(getOwner()) 
            ? Font.ITALIC : Font.PLAIN;
    }

    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_OPERATION;
    }
}
