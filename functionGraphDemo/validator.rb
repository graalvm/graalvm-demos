module FunctionValidator
  def self.validate(expr)
    allowedFunctions = ["sin", "cos", "log", "sqrt"]
    expr.split(/x|\+|\-|\*|\/|\^|\(|\)/).each{ |elm|
      if elm.length() > 0 then
        is_number = true if Float(elm) rescue false
        if !is_number then
          allowed = allowedFunctions.include? elm
          if !allowed then
            return "Unknown expression: " + elm
          end
        end
      end
    }
    ""
  end
end

Truffle::Interop.export :validator, FunctionValidator
