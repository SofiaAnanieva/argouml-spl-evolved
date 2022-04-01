// $Id: ActionRemoveModelElement.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

/**
 * Action to delete modelelements from the model without navigating
 * to/from them.  Used in UMLMutableList for deletion of modelelements
 * from the list.
 * @see org.argouml.uml.ui.ActionDeleteModelElements
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionRemoveModelElement extends AbstractActionRemoveElement {

    /**
     * The singleton.
     */
    public static final ActionRemoveModelElement SINGLETON =
	new ActionRemoveModelElement();

    /**
     * Constructor for ActionRemoveModelElement.
     */
    protected ActionRemoveModelElement() {
        super();
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Project p = ProjectManager.getManager().getCurrentProject();
        if (getObjectToRemove() != null
                && ActionDeleteModelElements.sureRemove(getObjectToRemove()))
            p.moveToTrash(getObjectToRemove());
        setObjectToRemove(null);
    }


    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return getObjectToRemove() != null;
    }

}
