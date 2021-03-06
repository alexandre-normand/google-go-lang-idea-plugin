package ro.redeul.google.go.lang.parser.parsing.expressions;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ro.redeul.google.go.lang.lexer.GoTokenTypeSets;
import ro.redeul.google.go.lang.parser.GoElementTypes;
import ro.redeul.google.go.lang.parser.GoParser;
import ro.redeul.google.go.lang.parser.parsing.util.ParserUtils;

/**
 * Created by IntelliJ IDEA.
 * User: mtoader
 * Date: Jul 28, 2010
 * Time: 3:02:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryExpression implements GoElementTypes {

    private IElementType elementType;
    private TokenSet operators;

    static BinaryExpression MUL_EXPR = new BinaryExpression(MUL_EXPRESSION, oMUL, oQUOTIENT, oREMAINDER, oSHIFT_LEFT, oSHIFT_RIGHT, oBIT_AND, oBIT_CLEAR);
    static BinaryExpression ADD_EXPR = new BinaryExpression(ADD_EXPRESSION, oPLUS, oMINUS, oBIT_OR, oBIT_XOR);
    static BinaryExpression REL_EXPR = new BinaryExpression(REL_EXPRESSION, oEQ, oNOT_EQ, oLESS, oLESS_OR_EQUAL, oGREATER, oGREATER_OR_EQUAL);
    static BinaryExpression COM_EXPR = new BinaryExpression(COM_EXPRESSION, oSEND_CHANNEL);
    static BinaryExpression LOG_AND_EXPR = new BinaryExpression(LOG_AND_EXPRESSION, oCOND_AND);
    static BinaryExpression LOG_OR_EXPR = new BinaryExpression(LOG_OR_EXPRESSION, oCOND_OR);

    static BinaryExpression precedence[] = {
            LOG_OR_EXPR,
            LOG_AND_EXPR,
            COM_EXPR,
            REL_EXPR,
            ADD_EXPR,
            MUL_EXPR,
    };

    public static boolean parse(PsiBuilder builder, GoParser parser, boolean inControlStmts) {
        return parse(builder, parser, inControlStmts, 0);
    }

    private static boolean parse(PsiBuilder builder, GoParser parser, boolean inControlStmts, int level) {

        ParserUtils.skipNLS(builder);

        PsiBuilder.Marker marker = builder.mark();
        if ( ! UnaryExpression.parse(builder, parser, inControlStmts) ) {
            marker.rollbackTo();
            return false;
        }

        boolean processedOperator = true;

        while ( processedOperator && GoTokenTypeSets.BINARY_OPERATORS.contains(builder.getTokenType()) ) {

            processedOperator = false;
            
            for ( int i = level; i < precedence.length; i++ ) {
                if ( precedence[i].operators.contains(builder.getTokenType()) ) {
                    ParserUtils.getToken(builder, builder.getTokenType());
                    ParserUtils.skipNLS(builder);

                    parse(builder, parser, inControlStmts, i + 1);

                    marker.done(precedence[i].elementType);
                    marker = marker.precede();
                    processedOperator = true;
                    break;
                }
            }
        }

        marker.drop();

        return true;
    }

    public BinaryExpression(IElementType elementType, IElementType ... operators) {
        this.elementType = elementType;
        this.operators = TokenSet.create(operators);
    }
}
