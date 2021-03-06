// $Id: FigSingleLineTextWithNotation.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.i18n.Translator;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;

import org.argouml.uml.diagram.ui.FigSingleLineText;

/**
 * A single line FigText that uses the Notation subsystem facilities 
 * for its text generation.
 * This FigText is editable by the user on the diagram.
 * 
 * @author Michiel
 */
public class FigSingleLineTextWithNotation extends FigSingleLineText 
    implements ArgoNotationEventListener {

    /**
     * The constructor.
     *
     * @param x the initial x position
     * @param y the initial y position
     * @param w the initial width
     * @param h the initial height
     * @param expandOnly true if the Fig should never shrink
     * @deprecated for 0.27.3 by mvw.  Use 
     * {@link #FigSingleLineTextWithNotation(Object, Rectangle, DiagramSettings,
     * boolean)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigSingleLineTextWithNotation(int x, int y, int w, int h, 
            boolean expandOnly) {
        super(x, y, w, h, expandOnly);
    }
    
    /**
     * The constructor.
     *
     * @param x the initial x position
     * @param y the initial y position
     * @param w the initial width
     * @param h the initial height
     * @param expandOnly true if this fig shall not shrink
     * @param allProperties the properties to listen to
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     * @deprecated for 0.27.3 by mvw.  Use 
     * {@link #FigSingleLineTextWithNotation(Object, Rectangle,
     * DiagramSettings, boolean)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigSingleLineTextWithNotation(int x, int y, int w, int h, 
            boolean expandOnly, 
            String[] allProperties) {
        super(x, y, w, h, expandOnly, allProperties);
    }

    /**
     * @param owner the owning UML object
     * @param bounds the initial position and size
     * @param settings the diagram settings applicable for this Fig
     * @param expandOnly true if the Fig won't shrink if its contents shrink
     */
    public FigSingleLineTextWithNotation(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly) {
        super(owner, bounds, settings, expandOnly);
        initNotationProviders();
    }

    /**
     * @param owner the owning UML object
     * @param bounds the initial position and size
     * @param settings the diagram settings applicable for this Fig
     * @param expandOnly true if the Fig won't shrink if its contents shrink
     * @param property name of property to listen to
     */
    public FigSingleLineTextWithNotation(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly, String property) {
        super(owner, bounds, settings, expandOnly, property);
        initNotationProviders();
    }

    /**
     * @param owner the owning UML object
     * @param bounds the initial position and size
     * @param settings the diagram settings applicable for this Fig
     * @param expandOnly true if the Fig won't shrink if its contents shrink
     * @param allProperties names of properties to listen to
     */
    public FigSingleLineTextWithNotation(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly, 
            String[] allProperties) {
        super(owner, bounds, settings, expandOnly, allProperties);
        initNotationProviders();
    }
    
    /**
     * @deprecated by mvw for 0.27.3. 
     * Remove this method completely 
     * once the deprecated constructors are gone.
     *
     * @param owner the uml element
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#setOwner(java.lang.Object)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public void setOwner(Object owner) {
        super.setOwner(owner);
        initNotationProviders();
    }

    /**
     * The notation provider for the text shown in this compartment.
     */
    private NotationProvider notationProvider;
    private HashMap<String, Object> npArguments = new HashMap<String, Object>();

    @Override
    public void removeFromDiagram() {
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        notationProvider.cleanListener(this, getOwner());
        super.removeFromDiagram();
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (notationProvider != null) {
            notationProvider.updateListener(this, getOwner(), pce);
        }
        super.propertyChange(pce);
    }
    
    /**
     * This is a template method called by the ArgoUML framework as the result
     * of a change to a model element. Do not call this method directly
     * yourself.
     * <p>Override this in any subclasses in order to redisplay the Fig
     * due to change of any model element that this Fig is listening to.</p>
     * <p>This method is guaranteed by the framework to be running on the 
     * Swing/AWT thread.</p>
     *
     * @param event the UmlChangeEvent that caused the change
     */
    protected void updateLayout(UmlChangeEvent event) {
        assert event != null;

        if (notationProvider != null
                && (!"remove".equals(event.getPropertyName())
                        || event.getSource() != getOwner())) { // not???
            this.setText(notationProvider.toString(getOwner(), 
                    getNotationSettings()));
            damage();
        }
    }

    /**
     * Overrule this for subclasses 
     * that need a different NotationProvider.
     * 
     * @return the type of the notation provider
     */
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_NAME;
    }

    /**
     * @return Returns the notationProvider for the text in this compartment.
     */
    public NotationProvider getNotationProvider() {
        return notationProvider;
    }

    /**
     * @param np The notationProvider to set.
     */
    void setNotationProvider(NotationProvider np) {
        if (notationProvider != null) {
            notationProvider.cleanListener(this, getOwner());
        }
        this.notationProvider = np;
        initNotationArguments();
    }

    /**
     * @return Returns the Notation Provider Arguments.
     * @deprecated for 0.27.3 by tfmorris.  Use {@link #getSettings()} then
     * getNotationSettings() on the settings object returned..
     */
    @Deprecated
    public HashMap<String, Object> getNpArguments() {
        return npArguments;
    }

    protected void initNotationProviders() {
        if (notationProvider != null) {
            notationProvider.cleanListener(this, getOwner());
        }
        if (getOwner() != null) {
            NotationName notation = Notation.findNotation(
                    getNotationSettings().getNotationLanguage());
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        getNotationProviderType(), getOwner(), this, notation);
            initNotationArguments();
        }
    }
    
    /**
     * @deprecated for 0.27.3 by tfmorris.  No replacement.
     */
    @Deprecated
    protected void initNotationArguments() {
        npArguments.put("useGuillemets", 
                getNotationSettings().isUseGuillemets());
    }

    @Deprecated
    protected void putNotationArgument(String key, Object element) {
        npArguments.put(key, element);
    }

    /**
     * Show the help-text for parsing, and initialise the text.
     */
    protected void textEditStarted() {
        String s = getNotationProvider().getParsingHelp();
        showHelp(s);
        setText();
    }

    /**
     * Parse the edited text to adapt the UML model.
     */
    protected void textEdited() {
        notationProvider.parse(getOwner(), getText());
        setText();
    }
    
    /**
     * @deprecated for 0.27.3 by tfmorris.  Diagrams are responsible for
     * updating their contained Figs with any notation changes.
     * MVW: There is no replacement yet!
     *
     * @param e the event
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    @Deprecated
    public void notationAdded(ArgoNotationEvent e) {
        // Do nothing
    }

    /**
     * @deprecated for 0.27.3 by tfmorris.  Diagrams are responsible for
     * updating their contained Figs with any notation changes.
     * MVW: There is no replacement yet!
     *
     * @param e the event
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    @Deprecated
    public void notationChanged(ArgoNotationEvent e) {
        renderingChanged();
    }
    
    /**
     * @deprecated for 0.27.3 by tfmorris.  Diagrams are responsible for
     * updating their contained Figs with any notation changes.
     * MVW: There is no replacement yet!
     *
     * @param e the event
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    @Deprecated
    public void notationProviderAdded(ArgoNotationEvent e) {
        // Do nothing
    }

    /**
     * @deprecated for 0.27.3 by tfmorris.  Diagrams are responsible for
     * updating their contained Figs with any notation changes.
     * MVW: There is no replacement yet!
     *
     * @param e the event
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    @Deprecated
    public void notationProviderRemoved(ArgoNotationEvent e) {
        // Do nothing    
    }

    /**
     * @deprecated for 0.27.3 by tfmorris. Diagrams are responsible for
     * updating their contained Figs with any notation changes.
     * MVW: There is no replacement yet!
     *
     * @param e the event
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    @Deprecated
    public void notationRemoved(ArgoNotationEvent e) {
        // Do nothing        
    }

    public void renderingChanged() {
        initNotationProviders();
        super.renderingChanged();
    }
    
    @Override
    protected void setText() {
        assert getOwner() != null;
        assert notationProvider != null;
        setText(notationProvider.toString(getOwner(), getNotationSettings()));
    }
    
    /**
     * Utility function to localize the given string with help text, and show it
     * in the status bar of the ArgoUML window. This function is used in favour
     * of the inline call to enable later improvements; e.g. it would be
     * possible to show a help-balloon.
     * <p>
     * TODO: This code is also present in other root Figs...
     * 
     * @param s the given string to be localized and shown
     */
    protected void showHelp(String s) {
        ArgoEventPump.fireEvent(new ArgoHelpEvent(
                ArgoEventTypes.HELP_CHANGED, this,
                Translator.localize(s)));
    }
    
    protected NotationSettings getNotationSettings() {
        return getSettings().getNotationSettings();
    }
}
