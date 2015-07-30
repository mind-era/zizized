/*
 * Copyright (c) 2015 Mind Eratosthenes Kft.
 * License: AGPL v3
 */
package com.mind_era.zizized.util

/**
 * TODO document package com.mind_era.zizized.util.Smt
 * 
 * @author Szabolcs Ivan
 * @since 1.0
 */
object Smt {
  
  def isSmt2SimpleSymbolChar( s : Char ) : Boolean =
        ('0' <= s && s <= '9') ||
        ('a' <= s && s <= 'z') ||
        ('A' <= s && s <= 'Z') ||
        s == '~' || s == '!' || s == '@' || s == '$' || s == '%' || s == '^' || s == '&' ||
        s == '*' || s == '_' || s == '-' || s == '+' || s == '=' || s == '<' || s == '>' ||
        s == '.' || s == '?' || s == '/'
  def isSmt2QuotedSymbol( s : Option[String] ) : Boolean = {
    s match {
      case None => false
      case Some(str) => if(('0' <= str(0) )&&( str(0) <= '9' )) true
                        else !str.forall( isSmt2SimpleSymbolChar(_))
      case _ => false // make the compiler happy
    } 
  }
  // TODO: symbol funcs  
}
/*

bool is_smt2_quoted_symbol(symbol const & s) {
    if (s.is_numerical())
        return false;
    return is_smt2_quoted_symbol(s.bare_str());
}

std::string mk_smt2_quoted_symbol(symbol const & s) {
    SASSERT(is_smt2_quoted_symbol(s));
    string_buffer<> buffer;
    buffer.append('|');
    char const * str = s.bare_str();
    while (*str) {
        if (*str == '|' || *str == '\\')
            buffer.append('\\');
        buffer.append(*str);
        str++;
    }
    buffer.append('|');
    return std::string(buffer.c_str());
}
}
* 
*/