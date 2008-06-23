/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import org.jvyamlb.events.PositionedStreamEndEvent;
import org.jvyamlb.events.PositionedStreamStartEvent;
import org.jvyamlb.tokens.Token;
import org.jvyamlb.events.StreamStartEvent;
import org.jvyamlb.events.StreamEndEvent;
import org.jvyamlb.events.DocumentStartEvent;
import org.jvyamlb.events.PositionedDocumentStartEvent;
import org.jvyamlb.events.PositionedDocumentEndEvent;
import java.util.Map;
import org.jvyamlb.events.DocumentEndEvent;
import org.jvyamlb.events.PositionedDocumentEndEvent;
import org.jvyamlb.events.ScalarEvent;
import org.jvyamlb.events.PositionedScalarEvent;
import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningParserImpl extends ParserImpl implements PositioningParser {
    protected static class PositioningProductionEnvironment extends ProductionEnvironment {
        public PositioningProductionEnvironment(final YAMLConfig cfg) {
            super(cfg);
        }

        protected StreamStartEvent getStreamStart(final Token t) {
            return new PositionedStreamStartEvent(((Positionable)t).getRange());
        }

        protected StreamEndEvent getStreamEnd(final Token t) {
            return new PositionedStreamEndEvent(((Positionable)t).getRange());
        }

        protected DocumentStartEvent getDocumentStart(final boolean explicit, final int[] version, final Map tags, final Token t) {
            return new PositionedDocumentStartEvent(explicit, version, tags, new Position.Range(((Positionable)t).getPosition()));
        }

        protected DocumentEndEvent getDocumentEndImplicit(final Token t) {
            return new PositionedDocumentEndEvent(false, new Position.Range(((Positionable)t).getPosition()));
        }

        protected DocumentEndEvent getDocumentEndExplicit(final Token t) {
            return new PositionedDocumentEndEvent(true, new Position.Range(((Positionable)t).getPosition()));
        }

        protected ScalarEvent getScalar(final String anchor, final String tag, final boolean[] implicit, final ByteList value, final char style, final Token t) {
            return new PositionedScalarEvent(anchor, tag, implicit, value, style, ((Positionable)t).getRange());
        }
    }
    
    public PositioningParserImpl(final PositioningScanner scanner) {
        super(scanner);
    }

    public PositioningParserImpl(final PositioningScanner scanner, final YAMLConfig cfg) {
        super(scanner, cfg);
    }

    public Position getPosition() {
        return ((PositioningScanner)scanner).getPosition();
    }

    public Position.Range getRange() {
        return new Position.Range(((PositioningScanner)scanner).getPosition());
    }

    protected ProductionEnvironment getEnvironment(YAMLConfig cfg) {
        return new PositioningProductionEnvironment(cfg);
    }
}
