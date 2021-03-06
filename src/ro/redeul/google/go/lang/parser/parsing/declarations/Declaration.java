package ro.redeul.google.go.lang.parser.parsing.declarations;

import com.intellij.lang.PsiBuilder;
import ro.redeul.google.go.lang.parser.GoElementTypes;
import ro.redeul.google.go.lang.parser.GoParser;
import ro.redeul.google.go.lang.parser.parsing.util.ParserUtils;

/**
 * Created by IntelliJ IDEA.
 * User: mtoader
 * Date: Jul 25, 2010
 * Time: 12:03:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Declaration implements GoElementTypes {

    public static boolean parse(PsiBuilder builder, GoParser goParser) {

        if ( ParserUtils.lookAhead(builder, kCONST) ) {
            return ConstDeclaration.parse(builder, goParser);
        }
                
        if ( ParserUtils.lookAhead(builder, kVAR) ) {
            return VarDeclaration.parse(builder, goParser);
        }

        if ( ParserUtils.lookAhead(builder, kTYPE) ) {
            return TypeDeclaration.parse(builder, goParser);
        }

        return false;
    }
}
