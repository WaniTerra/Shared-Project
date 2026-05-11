#ifndef FUNCTIONS_H
#define FUNCTIONS_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>
#include "structs.h" 

extern int dmg1_out;
extern int dmg2_out;
extern int moveList[48];
extern int moveListCount;
extern int globalUsedMoves[486];


int moveslist(int moveList[]);
void assignMovesToPokemon(Pokemon *p, Move move[]);

void initializeTypes(Type types[]);
void initializeMoves(Move move[], Type type[]);
void initializePokemons(Pokemon pokemons[], Type type[]);


void initialize(Type type[], Move move[], Pokemon pokemon[], Player *player1, Player *player2);
void game(Player *p1, Player *p2);

int getValidInt(int min, int max);
double calculateDamage(Player *attacker, Player *defender, int moveIndex);
int checkFainted(Player *p);
void applyDamage(Player *p1, Player *p2, int actionp1, int actionp2, int *dmg1_out, int *dmg2_out);
int playerFaintedPokemons(Player *p);

#endif