package es.codeurjc.model;

/**
 * DTO for transfer amounts without validation.
 * Used in controllers to avoid validation exceptions during parameter binding.
 * Validation is performed later in the service layer where errors can be properly handled.
 */
public class AmountDTO {
    private Double value;

    /**
     * Constructor that accepts any double value without validation.
     * @param value The numeric value of the amount (can be any value).
     */
    public AmountDTO(Double value) {
        this.value = value;
    }

    /**
     * Constructor that accepts a string representation of the amount.
     * @param value The string representation of the amount.
     */
    public AmountDTO(String value) {
        try {
            this.value = value != null && !value.isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            this.value = null;
        }
    }

    public Double getValue() {
        return value;
    }

    /**
     * Converts this DTO to an Amount object with validation.
     * @return An Amount object if the value is valid.
     * @throws IllegalArgumentException if the value is invalid.
     */
    public Amount toAmount() {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return new Amount(value);
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        return String.format("%.2f", value);
    }
}
