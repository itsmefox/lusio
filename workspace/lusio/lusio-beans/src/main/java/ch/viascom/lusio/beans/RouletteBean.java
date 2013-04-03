package ch.viascom.lusio.beans;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.entity.Field;
import ch.viascom.lusio.entity.Game;
import ch.viascom.lusio.entity.Tip;
import ch.viascom.lusio.module.GameStatus;

public class RouletteBean {

    @Inject
    EntityManager em;

    @Inject
    GameDBBean gameDBBean;

    @Inject
    TipDBBean tipDBBean;

    @Inject
    AccountDBBean accountDBBean;

    public void processGame() throws ServiceException {
        int outgoing = 0;

        // Get Open-Game
        Game game = gameDBBean.getOpenGame();
        String gameId = game.getGame_ID();
        
        // Set Game to PROCESSING
        setGameStatus(gameId, GameStatus.PROCESSING);

        // Get Field
        int field = calculateNumber();
        Field fieldObject = gameDBBean.getField(Integer.toString(field));

        // Number-Wins (*36)
        List<Tip> tipsNumberWin = tipDBBean.getTipsByField(fieldObject.getField_ID(),gameId);
        outgoing += payout(tipsNumberWin, 36);

        // Color-Wins
        String color = null;
        if (fieldObject.getColor() != null && fieldObject.getColor() == "R") {
            color = "RED";
        } else if (fieldObject.getColor() != null && fieldObject.getColor() == "B") {
            color = "BLACK";
        }
        if (color != null) {
            Field colorField = gameDBBean.getField(color);
            List<Tip> tipsColorWin = tipDBBean.getTipsByField(colorField.getField_ID(),gameId);
            outgoing += payout(tipsColorWin, 2);
        }

        // ODD/EVEN-Wins
        String evenOdd = null;
        if (field % 2 == 0) {
            evenOdd = "EVEN";
        } else {
            evenOdd = "ODD";
        }
        if (evenOdd != null) {
            Field evenOddField = gameDBBean.getField(evenOdd);
            List<Tip> tipsEvenOddWin = tipDBBean.getTipsByField(evenOddField.getField_ID(),gameId);
            outgoing += payout(tipsEvenOddWin, 2);
        }

        // 1-18/19-36 Wins
        String numbersHalf = null;
        if (field >= 1 && field <= 18) {
            numbersHalf = "1to18";
        } else if (field >= 19 && field <= 36) {
            numbersHalf = "19to36";
        }
        if (numbersHalf != null) {
            Field numbersHalfField = gameDBBean.getField(numbersHalf);
            List<Tip> tipsNumbersHalfWin = tipDBBean.getTipsByField(numbersHalfField.getField_ID(),gameId);
            outgoing += payout(tipsNumbersHalfWin, 2);
        }

        // 1st12 / 2nd12 / 3rd12 Wins
        String numbersTripple = null;
        if (field >= 1 && field <= 12) {
            numbersTripple = "1st12";
        } else if (field >= 13 && field <= 24) {
            numbersTripple = "2nd12";
        } else if (field >= 25 && field <= 36) {
            numbersTripple = "3rd12";
        }
        if (numbersTripple != null) {
            Field numbersTrippleField = gameDBBean.getField(numbersTripple);
            List<Tip> tipsNumbersTrippleWin = tipDBBean.getTipsByField(numbersTrippleField.getField_ID(),gameId);
            outgoing += payout(tipsNumbersTrippleWin, 3);
        }

        // 2to1 Wins
        Integer[] twoToOneR = { 1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34 };
        Integer[] twoToOneM = { 2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35 };
        Integer[] twoToOneL = { 3, 6, 9, 12, 15, 16, 21, 24, 27, 30, 33, 36 };
        String numbersTwoToOne = null;
        if (Arrays.asList(twoToOneR).contains(field)) {
            numbersTwoToOne = "2to1-r";
        }
        if (Arrays.asList(twoToOneM).contains(field)) {
            numbersTwoToOne = "2to1-m";
        }
        if (Arrays.asList(twoToOneL).contains(field)) {
            numbersTwoToOne = "2to1-l";
        }
        if (numbersTwoToOne != null) {
            Field numbersTwotoOneField = gameDBBean.getField(numbersTwoToOne);
            List<Tip> tipsNumbersTwotoOneWin = tipDBBean.getTipsByField(numbersTwotoOneField.getField_ID(),gameId);
            outgoing += payout(tipsNumbersTwotoOneWin, 3);
        }

        // Set Outgoing
        gameDBBean.setOutgoing(gameId, outgoing);
        
        // Set Income
        gameDBBean.setIncome(gameId, gameDBBean.getIncome(gameId));

        // Set Winfield
        gameDBBean.setWinField(gameId, fieldObject);

        // Delete Tips
        
        
        // Set Game to CLOSED
        setGameStatus(gameId, GameStatus.CLOSED);
    }

    public int payout(List<Tip> tips, int factor) {
        int outgoing = 0;
        for (Iterator<Tip> iterator = tips.iterator(); iterator.hasNext();) {
            Tip tip = (Tip) iterator.next();
            outgoing += tip.getAmount() * factor;
            accountDBBean.payout(tip.getUser().getUser_ID(), tip.getAmount() * factor);
        }
        return outgoing;
    }

    public int calculateNumber() {
        // Random number from rounds and jumps
        int randomStart = 0 + (int) (Math.random() * ((36 - 0) + 1));
        int randomRounds = 0 + (int) (Math.random() * ((36 - 0) + 1));
        int randomJumps = 2 + (int) (Math.random() * ((6 - 2) + 1));

        int field = calculateJump(randomStart, randomRounds, 1);

        for (int j = 1; j <= randomJumps; j++) {
            int randomDirection = 0 + (int) (Math.random() * ((1 - 0) + 1));
            int randomField = 1;
            if (randomDirection == 1) {
                randomField = 1 + (int) (Math.random() * ((3 - 1) + 1));
            } else {
                randomField = 2 + (int) (Math.random() * ((10 - 2) + 1));
            }
            field = calculateJump(field, randomField, randomDirection);
        }

        return field;

    }

    public int calculateJump(int currentField, int jumps, int direction) {
        int jumpCounter = jumps;
        int i = currentField;

        while (jumpCounter > 0) {
            jumpCounter -= 1;
            if (jumpCounter == 0) {
                return i;
            }
            if (i == 37) {
                i = 0;
            }
            if (i == -1) {
                i = 36;
            }
            if (direction == 1) {
                i++;
            } else {
                i--;
            }
        }
        return i;
    }

    private void setGameStatus(String gameId, GameStatus status) throws ServiceException {
        int statusInt = 0;

        em.getTransaction().begin();

        Game game = gameDBBean.getGame(gameId);

        switch (status) {
            case CLOSED:
                statusInt = 2;
                break;
            case OPEN:
                statusInt = 0;
                break;
            case PROCESSING:
                statusInt = 1;
        }

        game.setStatus(statusInt);

        em.merge(game);
        em.getTransaction().commit();
    }
}
