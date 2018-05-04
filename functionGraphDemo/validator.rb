module FunctionValidator
  ALLOWED_FUNCTIONS = %w[sin cos log sqrt]

  def self.validate(expr)
    expr.split(/x|[-+*\/()^]/).each do |term|
      unless term.empty?
        is_number = (Float(term) rescue false)
        unless is_number
          unless ALLOWED_FUNCTIONS.include? term
            return "Unknown expression: #{term}"
          end
        end
      end
    end
    ""
  end
end

Polyglot.export :Validator, FunctionValidator
