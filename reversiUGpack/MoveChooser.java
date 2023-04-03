import java.util.ArrayList;

import javax.lang.model.util.ElementScanner6;  

public class MoveChooser {

    //Setting up the array that holds the location values for all the
    //places on the board
    private static int[][] locationValues = {
        {120, -20, 20, 5, 5, 20, -20, 120},
        {-20, -40, -5, -5, -5, -5, -40, -20},
        {20, -5, 15, 3, 3, 15, -5, 20},
        {5, -5, 3, 3, 3, 3, -5, 5},
        {5, -5, 3, 3, 3, 3, -5, 5},
        {20, -5, 15, 3, 3, 15, -5, 20},
        {-20, -40, -5, -5, -5, -5, -40, -20},
        {120, -20, 20, 5, 5, 20, -20, 120}
    };

    //calculates the value of the board whilst it is in a specific state 
    public static int Evaluation(BoardState boardState)
    {
        int value = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8;  j++)
            {
                //loops through every board and checks if a peice is on it and if so
                //which colour it is and adds or subtracts accordingly
                if(boardState.getContents(i, j) == 1)
                {
                    value = value + locationValues[i][j];
                }
                else if(boardState.getContents(i, j) == -1)
                {
                    value = value - locationValues[i][j];
                }
            }
        }
        return value;
    }
  
    public static Move chooseMove(BoardState boardState){

	    int searchDepth= Othello.searchDepth;

        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
	    }
        int currentBest = 0;
        int movePos = 0;
        for (int i = 0; i < moves.size(); i++)
        {
            Move move = moves.get(i);
            BoardState tempBoardState = boardState.deepCopy();
            tempBoardState.makeLegalMove(move.x, move.y);

            //checks if the next move checked was better than the previous best move found
            if (currentBest >= miniMax(tempBoardState, searchDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, tempBoardState.colour))
            {
                movePos = i; 
            }

        }

        return moves.get(movePos);
    }

    //minimax algorithm that incorperates alpha beta pruning
    public static int miniMax(BoardState boardState, int depth, int alpha, int beta, int colour)
    {
        ArrayList<Move> moves = boardState.getLegalMoves();

        //Ends the recursive function and returns the value it has calculated through the calls
        if (depth == 0)
        {
            return Evaluation(boardState);
        }
        else if(moves.isEmpty())
        {
            if (colour == 1)
            {
                colour = -1;
            }
            else
            {
                colour = 1;
            }
            return miniMax(boardState, Othello.searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, colour);
        }
        else if(colour == 1)
        {
            //goes through each move in moves and checks value of board after move
            //uses value to decide if move is better than previous best
            for (Move move : moves)
            {
                BoardState tempBoardState = boardState.deepCopy();
                tempBoardState.makeLegalMove(move.x, move.y);
                alpha = Math.max(alpha, miniMax(tempBoardState, depth - 1, alpha, beta, colour));
            }
            return alpha;
        }
        else{
            for (Move move : moves)
            {
                BoardState tempBoardState = boardState.deepCopy();
                tempBoardState.makeLegalMove(move.x, move.y);
                beta = Math.min(alpha, miniMax(tempBoardState, depth - 1, alpha, beta, colour));
            }
            return beta;
        }
    }
}
