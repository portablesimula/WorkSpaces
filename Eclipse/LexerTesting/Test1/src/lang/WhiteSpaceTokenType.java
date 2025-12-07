package lang;

class WhiteSpaceTokenType extends IElementType { // implements IReparseableLeafElementType<ASTNode> {
	WhiteSpaceTokenType() {
		super("WHITE_SPACE", Language.ANY);
	}

//	@Override
//	public ASTNode reparseLeaf(ASTNode leaf, CharSequence newText) {
//		Language contextLanguage = leaf.getPsi().getLanguage();
//		if (contextLanguage == Language.ANY) {
//			return null;
//		}
//
//		ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(contextLanguage);
//		if (parserDefinition == null) {
//			return null;
//		}
//
//		for (int i = 0; i < newText.length(); i++) {
//			if (!Character.isWhitespace(newText.charAt(i))) {
//				return null;
//			}
//		}
//
//		return parserDefinition.reparseSpace(leaf, newText);
//	}
}
