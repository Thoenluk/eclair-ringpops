package main.java.ui.libraries;

import javax.swing.text.*;
import java.awt.*;

/*
 *  Code shamelessly stolen from https://tips4java.wordpress.com/2008/10/28/rectangle-painter/
 *  Given this code was written before I could do long division (not that I can do it properly now), and I DID NOT
 *  CREATE IT, just restating to make it clear, it is left intentionally unchanged but for applying a Code Cleanup so
 *  that IntelliJ will stop yelling at me.
 *  I could change the variables to Cleaner names, refactor the code, and altogether pretend I understand an iota of it.
 *  But I'd like to think the person who will hire me would appreciate honesty.
 */
public class SquigglePainter extends DefaultHighlighter.DefaultHighlightPainter {
    public SquigglePainter(final Color color) {
        super(color);
    }

    /**
     * Paints a portion of a highlight.
     *
     * @param g      the graphics context
     * @param offs0  the starting model offset >= 0
     * @param offs1  the ending model offset >= offs1
     * @param bounds the bounding box of the view, which is not
     *               necessarily the region to paint.
     * @param c      the editor
     * @param view   View painting for
     * @return region drawing occured in
     */
    public Shape paintLayer(final Graphics g, final int offs0, final int offs1, final Shape bounds, final JTextComponent c, final View view) {
        final Rectangle r = getDrawingArea(offs0, offs1, bounds, view);

        if (r == null) return null;

        //  Do your custom painting

        final Color color = getColor();
        g.setColor(color == null ? c.getSelectionColor() : color);

        //  Draw the squiggles

        final int squiggle = 2;
        final int twoSquiggles = squiggle * 2;
        final int y = r.y + r.height - squiggle;

        for (int x = r.x; x <= r.x + r.width - twoSquiggles; x += twoSquiggles) {
            g.drawArc(x, y, squiggle, squiggle, 0, 180);
            g.drawArc(x + squiggle, y, squiggle, squiggle, 180, 181);
        }

        // Return the drawing area

        return r;
    }


    private Rectangle getDrawingArea(final int offs0, final int offs1, final Shape bounds, final View view) {
        // Contained in view, can just use bounds.

        if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
            final Rectangle alloc;

            if (bounds instanceof Rectangle) {
                alloc = (Rectangle) bounds;
            } else {
                alloc = bounds.getBounds();
            }

            return alloc;
        } else {
            // Should only render part of View.
            try {
                // --- determine locations ---
                final Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);

                return (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();
            } catch (final BadLocationException e) {
                // can't render
            }
        }

        // Can't render

        return null;
    }
}