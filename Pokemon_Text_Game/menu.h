#ifndef MENU_H
#define MENU_H

#include "structs.h" 

#define COL_WIDTH 57
#define COL_HEIGHT 26

#define POKEMON_NAME 15
#define POKEMON_ATTACK_NAME 30

extern char p1_pokemons1[POKEMON_NAME];
extern char p1_pokemons2[POKEMON_NAME];
extern char p1_pokemons3[POKEMON_NAME];
extern char p1_pokemons4[POKEMON_NAME];
extern char p1_pokemons5[POKEMON_NAME];
extern char p1_pokemons6[POKEMON_NAME];

extern char p2_pokemons1[POKEMON_NAME];
extern char p2_pokemons2[POKEMON_NAME];
extern char p2_pokemons3[POKEMON_NAME];
extern char p2_pokemons4[POKEMON_NAME];
extern char p2_pokemons5[POKEMON_NAME];
extern char p2_pokemons6[POKEMON_NAME];

extern char old_pokemon_p1[POKEMON_NAME];
extern char new_pokemon_p1[POKEMON_NAME];

extern char old_pokemon_p2[POKEMON_NAME];
extern char new_pokemon_p2[POKEMON_NAME];

extern char current_name_p1_p[POKEMON_NAME];
extern int current_hp_p1_p;
extern int max_hp_p1_p;
extern int speed_p1_p;
extern int attack_p1_p;
extern int defense_p1_p;
extern int sp_attack_p1_p;
extern int sp_defense_p1_p;

extern char current_name_p2_p[POKEMON_NAME];
extern int current_hp_p2_p;
extern int max_hp_p2_p;
extern int speed_p2_p;
extern int attack_p2_p;
extern int defense_p2_p;
extern int sp_attack_p2_p;
extern int sp_defense_p2_p;

extern char pokemon_p1_type1[20];
extern char pokemon_p1_type2[20];
extern char pokemon_p2_type1[20];
extern char pokemon_p2_type2[20];

extern int p1_faintedPokemon;
extern int p2_faintedPokemon;

void center_text(char *dest, const char *text, int width);
void align_left(char *dest, const char *text, int width);
void align_right(char *dest, const char *text, int width);
void splitName(char *source, char *part1, char *part2);

void playerSetup(Player *pl1, Player *pl2);

void p1MainMenu(Player *pl1, Player *pl2);
void p1AttackChoose(Player *pl1, Player *pl2);
void p1ChangePokemon(Player *pl1, Player *pl2);
void p1ActionToP2MainMenu(int action, Player *pl1, Player *pl2, int actionO);

void p2AttackChoose(Player *pl1, Player *pl2, int action, int actionO);
void p2ChangePokemon(Player *pl1, Player *pl2, int action, int actionO);

void generalMessage(int action, Player *pl1, Player *pl2, int p1ActionN, int p2ActionN, int realDmg1, int realDmg2);

void printInvalid();
void printFainted();
void printSame();

#endif