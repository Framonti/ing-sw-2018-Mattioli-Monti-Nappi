package it.polimi.se2018.testModel;

import it.polimi.se2018.utilities.ToolCardDeckBuilder;
import it.polimi.se2018.model.Deck;
import it.polimi.se2018.model.ToolCard;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the ToolCardDeckBuilder Class
 * @author Framonti
 */
public class TestToolCardDeckBuilder {

    private final String filePath = "src/main/java/it/polimi/se2018/xml/ToolCard.xml";
    private ToolCardDeckBuilder toolCardDeckBuilder;

    private boolean checkToolCard(ToolCard toolCard){

        return  (toolCard.getName().equals("Pinza Sgrossatrice")&& toolCard.getDescription().equals("Dopo aver scelto und dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in un 1 o un 1 in un 6") && toolCard.getId() == 1)||
                (toolCard.getName().equals("Pennello per Eglomise")&& toolCard.getDescription().equals("Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore. Devi rispettare tutte le altre restrizioni di piazzamento" ) && toolCard.getId() == 2)||
                (toolCard.getName().equals("Alesatore per lamina di rame")&& toolCard.getDescription().equals("Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore. Devi rispettare tutte le altre restrizioni di piazzamento") && toolCard.getId() == 3)||
                (toolCard.getName().equals("Lathekin")&& toolCard.getDescription().equals("Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento") && toolCard.getId() == 4)||
                (toolCard.getName().equals("Taglierina circolante")&& toolCard.getDescription().equals("Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei Round") && toolCard.getId() == 5)||
                (toolCard.getName().equals("Pennello per Pasta Salda")&& toolCard.getDescription().equals("Dopo aver scelto un dado, tira nuovamente quel dado. Se non puoi piazzarlo, riponilo nella riserva") && toolCard.getId() == 6)||
                (toolCard.getName().equals("Martelletto")&& toolCard.getDescription().equals("Tira nuovamente tutti i dadi della Riserva. Questa carta può essere usata solo durante il tuo secondo turno, prima di scegliere il secondo dado") && toolCard.getId() == 7)||
                (toolCard.getName().equals("Tenaglia a rotelle")&& toolCard.getDescription().equals("Dopo il tuo primo turno, scegli immediatamente un altro dado. Salta il tuo secondo turno in questo round") && toolCard.getId() == 8)||
                (toolCard.getName().equals("Riga in Sughero")&& toolCard.getDescription().equals("Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente ad un altro dado. Devi rispettare tutte le restrizioni di piazzamento") && toolCard.getId() == 9)||
                (toolCard.getName().equals("Tampone Diamantato")&& toolCard.getDescription().equals("Dopo aver scelto un dado, giralo sulla facciata opposta. 6 diventa 1, 5 diventa 2, 4 diventa 3 ecc.") && toolCard.getId() == 10)||
                (toolCard.getName().equals("Diluente per Pasta Salda")&& toolCard.getDescription().equals("Dopo aver scelto un dado, riponilo nel Sacchetto, poi pescane uno dal Sacchetto. Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento") && toolCard.getId() == 11)||
                (toolCard.getName().equals("Taglierina Manuale")&& toolCard.getDescription().equals("Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato del Round. Devi rispettare tutte le restrizioni di piazzamento") && toolCard.getId() == 12);
    }

    @Before
    public void setUp(){

        toolCardDeckBuilder = new ToolCardDeckBuilder(filePath);
    }

    /**
     * Tests that every ToolCard is correctly created
     */
    @Test
    public void testGetWindowPatternCardDeck(){

        Deck<ToolCard> toolCardDeck = toolCardDeckBuilder.getToolCardDeck();
        List<ToolCard> toolCards = toolCardDeck.mixAndDistribute(12);

        for(ToolCard toolCard : toolCards){

            assertTrue(checkToolCard(toolCard));
        }
    }
}
