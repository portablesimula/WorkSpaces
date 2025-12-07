package lang.highlighter;

import lang.IElementType;
import lang.lexer.Lexer;

/**
 * Controls the syntax highlighting of a file.
 *
 * Extend {@link SyntaxHighlighterBase}.
 *
 * @see SyntaxHighlighterFactory#getSyntaxHighlighter(com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)
 * @see SyntaxHighlighterFactory#getSyntaxHighlighter(com.intellij.lang.Language, com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)
 */
public interface SyntaxHighlighter {

//  @ApiStatus.Internal
//  ExtensionPointName<KeyedFactoryEPBean> EP_NAME = ExtensionPointName.create("com.intellij.syntaxHighlighter");
//
//  /**
//   * @deprecated see
//   * {@link SyntaxHighlighterFactory#getSyntaxHighlighter(com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)} and
//   * {@link SyntaxHighlighterFactory#getSyntaxHighlighter(com.intellij.lang.Language, com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)}
//   */
//  @SuppressWarnings("DeprecatedIsStillUsed")
//  @Deprecated(forRemoval = true)
//  SyntaxHighlighterProvider PROVIDER = new FileTypeExtensionFactory<>(SyntaxHighlighterProvider.class, EP_NAME).get();

  /**
   * Returns the lexer used for highlighting the file. The lexer is invoked incrementally when the file is changed, so it must be
   * capable of saving/restoring state and resuming lexing from the middle of the file.
   *
   * @return The lexer implementation.
   */
  Lexer getHighlightingLexer();

  /**
   * Returns the list of text attribute keys used for highlighting the specified token type. The attributes of all attribute keys
   * returned for the token type are successively merged to obtain the color and attributes of the token.
   *
   * @param tokenType The token type for which the highlighting is requested.
   * @return The array of text attribute keys.
   */
//  TextAttributesKey[] getTokenHighlights(IElementType tokenType);
}
