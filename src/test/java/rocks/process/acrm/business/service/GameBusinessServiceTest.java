package rocks.process.acrm.business.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rocks.process.acrm.data.domain.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GameBusinessServiceTest {

    @Autowired
    private GameBusinessService gameBusinessService;

    private GameHandler gameHandler = new GameHandler();

    private void dataForPassTokenTest(){
        List<Player> playerList = new ArrayList<>();

        Game game = new Game();
        gameBusinessService.saveGame(game);

        Player player1 = new Player();
        player1.setPlaying(true);
        setGameAndSavePlayer(game, player1);
        Player player2 = new Player();
        setGameAndSavePlayer(game, player2);
        Player player3 = new Player();
        setGameAndSavePlayer(game, player3);
        Player player4 = new Player();
        setGameAndSavePlayer(game, player4);

        List<Card> handPlayer1 = new ArrayList<>();
        Card card1 = new Card();
        card1.setSuit(Suit.JADE);
        card1.setRank(2);
        gameBusinessService.saveCard(card1);
        handPlayer1.add(card1);
        player1.setHand(handPlayer1);
        gameBusinessService.savePlayer(player1);

        List<Card> handPlayer3 = new ArrayList<>();
        Card card2 = new Card();
        card2.setSuit(Suit.JADE);
        card2.setRank(2);
        gameBusinessService.saveCard(card2);
        handPlayer3.add(card2);
        player3.setHand(handPlayer3);
        gameBusinessService.savePlayer(player3);

        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);

        game.setPlayers(playerList);
        gameBusinessService.saveGame(game);

        gameHandler.setPlayerID(player1.getId());
        gameHandler.setGameID(game.getGameId());
    }

    @Test
    @Transactional
    void passTokenTest(){
        dataForPassTokenTest();

        gameBusinessService.passToken(gameHandler);
        assertTrue(gameBusinessService.getGame(gameHandler.getGameID()).getPlayers().get(2).isPlaying());
    }

    private void setGameAndSavePlayer(Game game, Player player1) {
        player1.setGame(game);
        gameBusinessService.savePlayer(player1);
    }
}