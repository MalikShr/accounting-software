package financialManagement.kennzahl;

import utils.Enums;
import utils.HelpFunctions;

public class Kennzahl {
    public String name;
    public String FormattedAmount;
    public Enums.KennzahlType type;

    public Kennzahl(String name, double amount, String sign, Enums.KennzahlType type) {
        this.name = name;
        this.FormattedAmount = HelpFunctions.Format(amount) + sign;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getFormattedAmount() {
        return FormattedAmount;
    }

    public Enums.KennzahlType getType() {
        return type;
    }
}
