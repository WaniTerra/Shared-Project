#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "structs.h"
#include "functions.h"

int main()
{
    
    /*Maximize the terminal size before running the code.
     Otherwise, you'll experience problems with the text.*/

    static Type Types[18];
    static Move Moves[486];
    static Pokemon Pokemons[1015];

    Player Player1;
    Player Player2;

    initialize(Types, Moves, Pokemons, &Player1, &Player2);

    game(&Player1, &Player2);

    return 0;
}
