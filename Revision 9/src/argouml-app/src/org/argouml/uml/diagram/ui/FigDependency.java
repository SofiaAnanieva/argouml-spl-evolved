// $Id: FigDependency.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;

import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathItemPlacement;

/**
 * This class represents a Fig for a Dependency.
 * It has a dashed line and a V-shaped arrow-head.
 * 
 * @author ics 125b course, spring 1998
 */
public class FigDependency extends FigEdgeModelElement {

    /**
     * Serial version generated by Eclipse for rev. 1.22
     */
    private static final long serialVersionUID = -1779182458484724448L;

    /*
     * Text group to contain name & stereotype
     */
    private FigTextGroup middleGroup; 

    /**
     * Constructor
     * @deprecated for 0.27.3 by tfmorris. Use
     *             {@link #FigDependency(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigDependency() {
        super();
        middleGroup = new FigTextGroup();
        constructFigs();
    }

    private void constructFigs() {
        middleGroup.addFig(getNameFig());
        middleGroup.addFig(getStereotypeFig());
        addPathItem(middleGroup,
                new PathItemPlacement(this, middleGroup, 50, 25));
        
        setDestArrowHead(createEndArrow());
        
        setBetweenNearestPoints(true);
        getFig().setDashed(true);
    }
    
    /**
     * Create the arrow head for the dependency. By default this is a
     * stick figure arrow head with no fill. Descendants may override this as
     * required.
     * @return the arrow head.
     */
    protected ArrowHead createEndArrow() {
        return new ArrowHeadGreater();
    }

    /**
     * Constructor that sets the UML element
     * 
     * @param dependency the UML element
     * @deprecated for 0.27.3 by tfmorris. Use
     *             {@link #FigDependency(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigDependency(Object dependency) {
        this();
        setOwner(dependency);
    }

    /**
     * @param dependency theUML element
     * @param lay the layer
     * @deprecated for 0.27.3 by tfmorris. Use
     *             {@link #FigDependency(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigDependency(Object dependency, Layer lay) {
        this();
        setOwner(dependency);
        setLayer(lay);
    }

    /**
     * Construct a FigDependency.
     * 
     * @param owner owning UML Dependency
     * @param settings render settings
     */
    public FigDependency(Object owner, DiagramSettings settings) {
        super(owner, settings);
        middleGroup = new FigTextGroup(owner, settings);
        constructFigs();
    }
    
    /*
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(true);
        // computeRoute();
        // this recomputes the route if you reload the diagram.
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    @Override
    protected boolean canEdit(Fig f) {
        return false;
    }

    public void setLineColor(Color color) {
        ArrowHead arrow = getDestArrowHead();
        if (arrow != null) {
            arrow.setLineColor(getLineColor());
        }
    }
    
    @Override
    protected void updateNameText() {
        super.updateNameText();
        middleGroup.calcBounds();
    }

    @Override
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        middleGroup.calcBounds();
    }

}

