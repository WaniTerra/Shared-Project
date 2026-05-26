#include <stdio.h>
#include <string.h>
#include "structs.h"
#include "menu.h"     
#include "functions.h"


double calculateDamage(Player *attacker, Player *defender, int moveIndex);
int playerFaintedPokemons(Player *p1);

char p1_pokemons1[POKEMON_NAME];
char p1_pokemons2[POKEMON_NAME];
char p1_pokemons3[POKEMON_NAME];
char p1_pokemons4[POKEMON_NAME];
char p1_pokemons5[POKEMON_NAME];
char p1_pokemons6[POKEMON_NAME];
char p2_pokemons1[POKEMON_NAME];
char p2_pokemons2[POKEMON_NAME];
char p2_pokemons3[POKEMON_NAME];
char p2_pokemons4[POKEMON_NAME];
char p2_pokemons5[POKEMON_NAME];
char p2_pokemons6[POKEMON_NAME];

char old_pokemon_p1[POKEMON_NAME];
char new_pokemon_p1[POKEMON_NAME];

char old_pokemon_p2[POKEMON_NAME];
char new_pokemon_p2[POKEMON_NAME];

char current_name_p1_p[POKEMON_NAME];
int current_hp_p1_p;
int max_hp_p1_p;
int speed_p1_p;
int attack_p1_p;
int defense_p1_p;
int sp_attack_p1_p;
int sp_defense_p1_p;

char current_name_p2_p[POKEMON_NAME];
int current_hp_p2_p;
int max_hp_p2_p;
int speed_p2_p;
int attack_p2_p;
int defense_p2_p;
int sp_attack_p2_p;
int sp_defense_p2_p;

char pokemon_p1_type1[20];
char pokemon_p1_type2[20];
char pokemon_p2_type1[20];
char pokemon_p2_type2[20];

int p1_faintedPokemon = 0;
int p2_faintedPokemon = 0;

void center_text(char *dest, const char *text, int width)
{
    int text_len = strlen(text);
    if (text_len >= width)
    {
        strcpy(dest, text);
    }
    else
    {
        int pad_left = (width - text_len) / 2;
        int pad_right = width - text_len - pad_left;

        sprintf(dest, "%*s%s%*s", pad_left, "", text, pad_right, "");
    }
}
void align_left(char *dest, const char *text, int width)
{
    sprintf(dest, "%-*s", width, text);
}
void align_right(char *dest, const char *text, int width)
{
    sprintf(dest, "%*s", width, text);
}
void splitName(char *source, char *part1, char *part2)
{
    int i;
    for (i = 0; i < 12; i++)
    {
        if (*(source + i) == '\0')
            break;
        *(part1 + i) = *(source + i);
    }
    *(part1 + i) = '\0';

    int j = 0;
    while (*(source + 12 + j) != '\0')
    {
        *(part2 + j) = *(source + 12 + j);
        j++;
    }
    *(part2 + j) = '\0';
}

void playerSetup(Player *pl1, Player *pl2)
{
    pokemon_p1_type1[0] = '\0';
    strcat(pokemon_p1_type1, pl1->pokemons[pl1->currentIndex].types[0].name);
    pokemon_p1_type2[0] = '\0';
    strcat(pokemon_p1_type2, pl1->pokemons[pl1->currentIndex].types[1].name);

    current_name_p1_p[0] = '\0';
    strcat(current_name_p1_p, pl1->pokemons[pl1->currentIndex].name);

    char *p1_display_slots[] = {p1_pokemons1, p1_pokemons2, p1_pokemons3, p1_pokemons4, p1_pokemons5, p1_pokemons6};

    for (int i = 0; i < 6; i++)
    {
        p1_display_slots[i][0] = '\0';
        if (pl1->pokemons[i].currentHP > 0)
        {
            strcat(p1_display_slots[i], pl1->pokemons[i].name);
        }
        else
        {
            strcat(p1_display_slots[i], "---");
        }
    }

    current_hp_p1_p = pl1->pokemons[pl1->currentIndex].currentHP;
    max_hp_p1_p = pl1->pokemons[pl1->currentIndex].maxHP;
    speed_p1_p = pl1->pokemons[pl1->currentIndex].speed;
    attack_p1_p = pl1->pokemons[pl1->currentIndex].attack;
    defense_p1_p = pl1->pokemons[pl1->currentIndex].defense;
    sp_attack_p1_p = pl1->pokemons[pl1->currentIndex].spAtk;
    sp_defense_p1_p = pl1->pokemons[pl1->currentIndex].spDef;

    pokemon_p2_type1[0] = '\0';
    strcat(pokemon_p2_type1, pl2->pokemons[pl2->currentIndex].types[0].name);
    pokemon_p2_type2[0] = '\0';
    strcat(pokemon_p2_type2, pl2->pokemons[pl2->currentIndex].types[1].name);

    current_name_p2_p[0] = '\0';
    strcat(current_name_p2_p, pl2->pokemons[pl2->currentIndex].name);

    char *p2_display_slots[] = {p2_pokemons1, p2_pokemons2, p2_pokemons3, p2_pokemons4, p2_pokemons5, p2_pokemons6};

    for (int i = 0; i < 6; i++)
    {
        p2_display_slots[i][0] = '\0';
        if (pl2->pokemons[i].currentHP > 0)
        {
            strcat(p2_display_slots[i], pl2->pokemons[i].name);
        }
        else
        {
            strcat(p2_display_slots[i], "---");
        }
    }

    current_hp_p2_p = pl2->pokemons[pl2->currentIndex].currentHP;
    max_hp_p2_p = pl2->pokemons[pl2->currentIndex].maxHP;
    speed_p2_p = pl2->pokemons[pl2->currentIndex].speed;
    attack_p2_p = pl2->pokemons[pl2->currentIndex].attack;
    defense_p2_p = pl2->pokemons[pl2->currentIndex].defense;
    sp_attack_p2_p = pl2->pokemons[pl2->currentIndex].spAtk;
    sp_defense_p2_p = pl2->pokemons[pl2->currentIndex].spDef;

    p1_faintedPokemon = playerFaintedPokemons(pl1);
    p2_faintedPokemon = playerFaintedPokemons(pl2);
}

void p1MainMenu(Player *pl1, Player *pl2)
{
    char screen[4096] = "";
    char line[200];

    char col_left[50];
    char col_right[50];
    char title_buffer[100];

    const char *p1 = pl1->name;
    const char *p2 = pl2->name;

    int total_width = (COL_WIDTH * 2) + 1;

    center_text(title_buffer, "POKEMON BATTLE ARENA", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    for (int i = 0; i < COL_HEIGHT; i++)
    {
        col_left[0] = '\0';
        col_right[0] = '\0';

        if (i == 1)
        {
            center_text(col_left, p1, COL_WIDTH);
            center_text(col_right, p2, COL_WIDTH);
        }
        else if (i == 3)
        {
            char p1PokemonNumbers[50];
            sprintf(p1PokemonNumbers, "Pokemons : 6 / %d", 6 - p1_faintedPokemon);

            char p2PokemonNumbers[50];
            sprintf(p2PokemonNumbers, "Pokemons : 6 / %d", 6 - p2_faintedPokemon);

            center_text(col_left, p1PokemonNumbers, COL_WIDTH);
            center_text(col_right, p2PokemonNumbers, COL_WIDTH);
        }
        else if (i == 8)
        {
            align_left(col_left, current_name_p1_p, COL_WIDTH);
            align_right(col_right, current_name_p2_p, COL_WIDTH);
        }
        else if (i == 9)
        {
            char p1TypeText[50];
            sprintf(p1TypeText, "%s - %s", pokemon_p1_type1, pokemon_p1_type2);
            char p2TypeText[50];
            sprintf(p2TypeText, "%s - %s", pokemon_p2_type1, pokemon_p2_type2);
            align_right(col_right, p2TypeText, COL_WIDTH);
            align_left(col_left, p1TypeText, COL_WIDTH);
        }
        else if (i == 15)
        {
            char textp1[50];
            sprintf(textp1, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p1_p, max_hp_p1_p, speed_p1_p, attack_p1_p, defense_p1_p, sp_attack_p1_p, sp_defense_p1_p);
            char textp2[50];
            sprintf(textp2, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p2_p, max_hp_p2_p, speed_p2_p, attack_p2_p, defense_p2_p, sp_attack_p2_p, sp_defense_p2_p);

            align_right(col_right, textp2, COL_WIDTH);
            align_left(col_left, textp1, COL_WIDTH);
        }
        else if (i < 18)
        {
            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }
        else if (i == 18)
        {
            center_text(col_left, "----------------------------------", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }
        else if (i == 19)
        {
            if (p1_faintedPokemon == 5)
                center_text(col_left, "|           1) Atttack           |", COL_WIDTH);
            else
                center_text(col_left, "|  1) Atttack   2) Change Pokemon  |", COL_WIDTH);

            center_text(col_right, "", COL_WIDTH);
        }
        else if (i == 20)
        {
            center_text(col_left, "----------------------------------", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        else
        {
            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        sprintf(line, "%s|%s\n", col_left, col_right);
        strcat(screen, line);
    }
    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);
    printf("%s", screen);
}
void p1AttackChoose(Player *pl1, Player *pl2)
{
    char screen[4096] = "";
    char line[200];

    char col_left[50];
    char col_right[50];
    char title_buffer[100];

    const char *p1 = pl1->name;
    const char *p2 = pl2->name;
    char a1[POKEMON_ATTACK_NAME];
    strcpy(a1, pl1->pokemons[pl1->currentIndex].moves[0].name);
    char a2[POKEMON_ATTACK_NAME];
    strcpy(a2, pl1->pokemons[pl1->currentIndex].moves[1].name);
    char a3[POKEMON_ATTACK_NAME];
    strcpy(a3, pl1->pokemons[pl1->currentIndex].moves[2].name);
    char a4[POKEMON_ATTACK_NAME];
    strcpy(a4, pl1->pokemons[pl1->currentIndex].moves[3].name);

    int total_width = (COL_WIDTH * 2) + 1;

    center_text(title_buffer, "POKEMON BATTLE ARENA", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    for (int i = 0; i < COL_HEIGHT; i++)
    {
        col_left[0] = '\0';
        col_right[0] = '\0';

        if (i == 1)
        {
            center_text(col_left, p1, COL_WIDTH);
            center_text(col_right, p2, COL_WIDTH);
        }
        else if (i == 3)
        {
            char p1PokemonNumbers[50];
            sprintf(p1PokemonNumbers, "Pokemons : 6 / %d", 6 - p1_faintedPokemon);

            char p2PokemonNumbers[50];
            sprintf(p2PokemonNumbers, "Pokemons : 6 / %d", 6 - p2_faintedPokemon);

            center_text(col_left, p1PokemonNumbers, COL_WIDTH);
            center_text(col_right, p2PokemonNumbers, COL_WIDTH);
        }
        else if (i == 8)
        {
            align_left(col_left, current_name_p1_p, COL_WIDTH);
            align_right(col_right, current_name_p2_p, COL_WIDTH);
        }
        else if (i == 9)
        {
            char p1TypeText[50];
            sprintf(p1TypeText, "%s - %s", pokemon_p1_type1, pokemon_p1_type2);
            char p2TypeText[50];
            sprintf(p2TypeText, "%s - %s", pokemon_p2_type1, pokemon_p2_type2);
            align_right(col_right, p2TypeText, COL_WIDTH);
            align_left(col_left, p1TypeText, COL_WIDTH);
        }
        else if (i == 15)
        {
            char textp1[50];
            sprintf(textp1, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p1_p, max_hp_p1_p, speed_p1_p, attack_p1_p, defense_p1_p, sp_attack_p1_p, sp_defense_p1_p);
            char textp2[50];
            sprintf(textp2, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p2_p, max_hp_p2_p, speed_p2_p, attack_p2_p, defense_p2_p, sp_attack_p2_p, sp_defense_p2_p);

            align_right(col_right, textp2, COL_WIDTH);
            align_left(col_left, textp1, COL_WIDTH);
        }
        else if (i == 18)
        {
            center_text(col_left, "Pokemon's Attacks", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }
        else if (i == 19)
        {
            center_text(col_left, "----------------------------------", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }
        else if (i >= 19 && i < 26)
        {
            char m1_p1[13] = "", m1_p2[50] = "";
            char m2_p1[13] = "", m2_p2[50] = "";
            char m3_p1[13] = "", m3_p2[50] = "";
            char m4_p1[13] = "", m4_p2[50] = "";

            if (strlen(a1) > 12)
                splitName(a1, m1_p1, m1_p2);
            else
                strcpy(m1_p1, a1);

            if (strlen(a2) > 12)
                splitName(a2, m2_p1, m2_p2);
            else
                strcpy(m2_p1, a2);

            if (strlen(a3) > 12)
                splitName(a3, m3_p1, m3_p2);
            else
                strcpy(m3_p1, a3);

            if (strlen(a4) > 12)
                splitName(a4, m4_p1, m4_p2);
            else
                strcpy(m4_p1, a4);

            if (i == 20)
            {

                char text[128];

                sprintf(text, "| 1) %-12s  2) %-12s |", m1_p1, m2_p1);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 21)
            {

                char text[128];
                sprintf(text, "|   %-12s    %-12s   |", m1_p2, m2_p2);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 22)
            {

                center_text(col_left, "|                                  |", COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 23)
            {

                char text[128];
                sprintf(text, "| 3) %-12s  4) %-12s |", m3_p1, m4_p1);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 24)
            {

                char text[128];
                sprintf(text, "|   %-12s    %-12s   |", m3_p2, m4_p2);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 25)
            {
                center_text(col_left, "----------------------------------", COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
        }
        else
        {
            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        sprintf(line, "%s|%s\n", col_left, col_right);
        strcat(screen, line);
    }
    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    printf("%s", screen);
}
void p1ChangePokemon(Player *pl1, Player *pl2)
{

    char screen[4096] = "";
    char line[200];

    char col_left[50];
    char col_right[50];
    char title_buffer[100];

    const char *p1 = pl1->name;
    const char *p2 = pl2->name;

    int total_width = (COL_WIDTH * 2) + 1;

    center_text(title_buffer, "POKEMON BATTLE ARENA", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    for (int i = 0; i < COL_HEIGHT; i++)
    {
        col_left[0] = '\0';
        col_right[0] = '\0';

        if (i == 1)
        {
            center_text(col_left, p1, COL_WIDTH);
            center_text(col_right, p2, COL_WIDTH);
        }
        else if (i == 3)
        {
            char p1PokemonNumbers[50];
            sprintf(p1PokemonNumbers, "Pokemons : 6 / %d", 6 - p1_faintedPokemon);

            char p2PokemonNumbers[50];
            sprintf(p2PokemonNumbers, "Pokemons : 6 / %d", 6 - p2_faintedPokemon);

            center_text(col_left, p1PokemonNumbers, COL_WIDTH);
            center_text(col_right, p2PokemonNumbers, COL_WIDTH);
        }
        else if (i == 8)
        {
            align_left(col_left, current_name_p1_p, COL_WIDTH);
            align_right(col_right, current_name_p2_p, COL_WIDTH);
        }
        else if (i == 9)
        {
            char p1TypeText[50];
            sprintf(p1TypeText, "%s - %s", pokemon_p1_type1, pokemon_p1_type2);
            char p2TypeText[50];
            sprintf(p2TypeText, "%s - %s", pokemon_p2_type1, pokemon_p2_type2);
            align_right(col_right, p2TypeText, COL_WIDTH);
            align_left(col_left, p1TypeText, COL_WIDTH);
        }
        else if (i == 15)
        {
            char textp1[50];
            sprintf(textp1, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p1_p, max_hp_p1_p, speed_p1_p, attack_p1_p, defense_p1_p, sp_attack_p1_p, sp_defense_p1_p);
            char textp2[50];
            sprintf(textp2, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p2_p, max_hp_p2_p, speed_p2_p, attack_p2_p, defense_p2_p, sp_attack_p2_p, sp_defense_p2_p);

            align_right(col_right, textp2, COL_WIDTH);
            align_left(col_left, textp1, COL_WIDTH);
        }
        else if (i == 18)
        {
            center_text(col_left, "Pokemon Change", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }
        else if (i == 19)
        {
            center_text(col_left, "----------------------------------", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }
        else if (i >= 19 && i < 26)
        {

            if (i == 20)
            {

                char text[128];

                sprintf(text, "| 1) %-12s  2) %-12s |", p1_pokemons1, p1_pokemons2);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 21)
            {

                char text[128];

                sprintf(text, "|                                  |");
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 22)
            {
                char text[128];
                sprintf(text, "| 3) %-12s  4) %-12s |", p1_pokemons3, p1_pokemons4);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 23)
            {

                char text[128];

                sprintf(text, "|                                  |");
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 24)
            {

                char text[128];
                sprintf(text, "| 5) %-12s  6) %-12s |", p1_pokemons5, p1_pokemons6);
                center_text(col_left, text, COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
            else if (i == 25)
            {
                center_text(col_left, "----------------------------------", COL_WIDTH);
                center_text(col_right, "", COL_WIDTH);
            }
        }
        else
        {
            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        sprintf(line, "%s|%s\n", col_left, col_right);
        strcat(screen, line);
    }
    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);
    printf("%s", screen);
}
void p1ActionToP2MainMenu(int action, Player *pl1, Player *pl2, int actionO)
{

    char screen[4096] = "";
    char line[200];

    char col_left[50];
    char col_right[50];
    char title_buffer[100];

    const char *p1 = pl1->name;
    const char *p2 = pl2->name;

    int total_width = (COL_WIDTH * 2) + 1;

    center_text(title_buffer, "POKEMON BATTLE ARENA", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    for (int i = 0; i < COL_HEIGHT; i++)
    {
        col_left[0] = '\0';
        col_right[0] = '\0';

        if (i == 1)
        {
            center_text(col_left, p1, COL_WIDTH);
            center_text(col_right, p2, COL_WIDTH);
        }
        else if (i == 3)
        {
            char p1PokemonNumbers[50];
            sprintf(p1PokemonNumbers, "Pokemons : 6 / %d", 6 - p1_faintedPokemon);

            char p2PokemonNumbers[50];
            sprintf(p2PokemonNumbers, "Pokemons : 6 / %d", 6 - p2_faintedPokemon);

            center_text(col_left, p1PokemonNumbers, COL_WIDTH);
            center_text(col_right, p2PokemonNumbers, COL_WIDTH);
        }
        else if (i == 8)
        {
            align_left(col_left, current_name_p1_p, COL_WIDTH);
            align_right(col_right, current_name_p2_p, COL_WIDTH);
        }
        else if (i == 9)
        {
            char p1TypeText[50];
            sprintf(p1TypeText, "%s - %s", pokemon_p1_type1, pokemon_p1_type2);
            char p2TypeText[50];
            sprintf(p2TypeText, "%s - %s", pokemon_p2_type1, pokemon_p2_type2);
            align_right(col_right, p2TypeText, COL_WIDTH);
            align_left(col_left, p1TypeText, COL_WIDTH);
        }
        else if (i == 15)
        {
            char textp1[50];
            sprintf(textp1, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p1_p, max_hp_p1_p, speed_p1_p, attack_p1_p, defense_p1_p, sp_attack_p1_p, sp_defense_p1_p);
            char textp2[50];
            sprintf(textp2, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p2_p, max_hp_p2_p, speed_p2_p, attack_p2_p, defense_p2_p, sp_attack_p2_p, sp_defense_p2_p);

            align_right(col_right, textp2, COL_WIDTH);
            align_left(col_left, textp1, COL_WIDTH);
        }
        else if (i >= 18 && i < 22)
        {
            switch (action)
            {
            case 1:

                if (i == 18)
                {
                    center_text(col_left, " Attack ", COL_WIDTH);
                    center_text(col_right, " Menu ", COL_WIDTH);
                }
                if (i == 19)
                {
                    center_text(col_left, "----------------------------------", COL_WIDTH);
                    center_text(col_right, "----------------------------------", COL_WIDTH);
                }
                else if (i > 19 && i < 22)
                {

                    if (i == 20)
                    {

                        char text[128];
                        char moveName[50];
                        strcpy(moveName, pl1->pokemons[pl1->currentIndex].moves[actionO - 1].name);
                        char centeredMove[50];
                        center_text(centeredMove, moveName, 32);
                        sprintf(text, "|%s|", centeredMove);
                        center_text(col_left, text, COL_WIDTH);
                        if (p2_faintedPokemon == 5)
                            center_text(col_right, "|           1) Atttack           |", COL_WIDTH);
                        else
                            center_text(col_right, "|  1) Atttack   2) Change Pokemon  |", COL_WIDTH);
                    }

                    else if (i == 21)
                    {
                        center_text(col_left, "----------------------------------", COL_WIDTH);
                        center_text(col_right, "----------------------------------", COL_WIDTH);
                    }
                }

                break;

            case 2:
                if (i == 18)
                {
                    center_text(col_left, " Change ", COL_WIDTH);
                    center_text(col_right, " Menu ", COL_WIDTH);
                }
                if (i == 19)
                {
                    center_text(col_left, "----------------------------------", COL_WIDTH);
                    center_text(col_right, "----------------------------------", COL_WIDTH);
                }
                else if (i > 19 && i < 22)
                {

                    if (i == 20)
                    {

                        char text[128];
                        char oldNameCentered[20];
                        char newNameCentered[20];

                        center_text(oldNameCentered, old_pokemon_p1, 13);
                        center_text(newNameCentered, new_pokemon_p1, 13);

                        sprintf(text, "| %s --> %s|", oldNameCentered, newNameCentered);
                        center_text(col_left, text, COL_WIDTH);
                        if (p2_faintedPokemon == 5)
                            center_text(col_right, "|           1) Atttack           |", COL_WIDTH);
                        else
                            center_text(col_right, "|  1) Atttack   2) Change Pokemon  |", COL_WIDTH);
                    }

                    else if (i == 21)
                    {
                        center_text(col_left, "----------------------------------", COL_WIDTH);
                        center_text(col_right, "----------------------------------", COL_WIDTH);
                    }
                }
                break;
            default:
                break;
            }
        }

        else
        {
            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        sprintf(line, "%s|%s\n", col_left, col_right);
        strcat(screen, line);
    }
    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);
    printf("%s", screen);
}

void p2AttackChoose(Player *pl1, Player *pl2, int action, int actionO)
{

    char screen[4096] = "";
    char line[200];

    char col_left[50];
    char col_right[50];
    char title_buffer[100];

    const char *p1_name = pl1->name;
    const char *p2_name = pl2->name;

    char a1[POKEMON_ATTACK_NAME];
    strcpy(a1, pl2->pokemons[pl2->currentIndex].moves[0].name);
    char a2[POKEMON_ATTACK_NAME];
    strcpy(a2, pl2->pokemons[pl2->currentIndex].moves[1].name);
    char a3[POKEMON_ATTACK_NAME];
    strcpy(a3, pl2->pokemons[pl2->currentIndex].moves[2].name);
    char a4[POKEMON_ATTACK_NAME];
    strcpy(a4, pl2->pokemons[pl2->currentIndex].moves[3].name);

    char m1_p1[13] = "", m1_p2[50] = "";
    char m2_p1[13] = "", m2_p2[50] = "";
    char m3_p1[13] = "", m3_p2[50] = "";
    char m4_p1[13] = "", m4_p2[50] = "";

    if (strlen(a1) > 12)
        splitName(a1, m1_p1, m1_p2);
    else
        strcpy(m1_p1, a1);

    if (strlen(a2) > 12)
        splitName(a2, m2_p1, m2_p2);
    else
        strcpy(m2_p1, a2);

    if (strlen(a3) > 12)
        splitName(a3, m3_p1, m3_p2);
    else
        strcpy(m3_p1, a3);

    if (strlen(a4) > 12)
        splitName(a4, m4_p1, m4_p2);
    else
        strcpy(m4_p1, a4);

    int total_width = (COL_WIDTH * 2) + 1;

    center_text(title_buffer, "POKEMON BATTLE ARENA", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    for (int i = 0; i < COL_HEIGHT; i++)
    {

        col_left[0] = '\0';
        col_right[0] = '\0';

        if (i == 1)
        {
            center_text(col_left, p1_name, COL_WIDTH);
            center_text(col_right, p2_name, COL_WIDTH);
        }
        else if (i == 3)
        {
            char p1PokemonNumbers[50];
            sprintf(p1PokemonNumbers, "Pokemons : 6 / %d", 6 - p1_faintedPokemon);

            char p2PokemonNumbers[50];
            sprintf(p2PokemonNumbers, "Pokemons : 6 / %d", 6 - p2_faintedPokemon);

            center_text(col_left, p1PokemonNumbers, COL_WIDTH);
            center_text(col_right, p2PokemonNumbers, COL_WIDTH);
        }
        else if (i == 8)
        {
            align_left(col_left, current_name_p1_p, COL_WIDTH);
            align_right(col_right, current_name_p2_p, COL_WIDTH);
        }

        else if (i == 9)
        {
            char p1TypeText[50];
            sprintf(p1TypeText, "%s - %s", pokemon_p1_type1, pokemon_p1_type2);
            char p2TypeText[50];
            sprintf(p2TypeText, "%s - %s", pokemon_p2_type1, pokemon_p2_type2);
            align_right(col_right, p2TypeText, COL_WIDTH);
            align_left(col_left, p1TypeText, COL_WIDTH);
        }
        else if (i == 15)
        {
            char textp1[50];
            sprintf(textp1, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p1_p, max_hp_p1_p, speed_p1_p, attack_p1_p, defense_p1_p, sp_attack_p1_p, sp_defense_p1_p);
            char textp2[50];
            sprintf(textp2, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p2_p, max_hp_p2_p, speed_p2_p, attack_p2_p, defense_p2_p, sp_attack_p2_p, sp_defense_p2_p);

            align_right(col_right, textp2, COL_WIDTH);
            align_left(col_left, textp1, COL_WIDTH);
        }
        else if (i == 18)
        {
            center_text(col_right, "Pokemon's Attacks", COL_WIDTH);
            if (action == 1)
                center_text(col_left, " Attack ", COL_WIDTH);
            else if (action == 2)
                center_text(col_left, " Change ", COL_WIDTH);
        }
        else if (i == 19)
        {
            center_text(col_right, "----------------------------------", COL_WIDTH);
            center_text(col_left, "----------------------------------", COL_WIDTH);
        }
        else if (i >= 20 && i < 26)
        {
            center_text(col_left, "", COL_WIDTH);

            if (i == 20)
            {
                char text[128];
                sprintf(text, "| 1) %-12s  2) %-12s |", m1_p1, m2_p1);
                center_text(col_right, text, COL_WIDTH);

                char text1[60];
                if (action == 1)
                {
                    char moveName[50];
                    strcpy(moveName, pl1->pokemons[pl1->currentIndex].moves[actionO - 1].name);
                    char centeredMove[50];
                    center_text(centeredMove, moveName, 32);
                    sprintf(text1, "|%s|", centeredMove);
                    center_text(col_left, text1, COL_WIDTH);
                }
                else if (action == 2)
                {
                    char oldNameCentered[20];
                    char newNameCentered[20];

                    center_text(oldNameCentered, old_pokemon_p1, 13);
                    center_text(newNameCentered, new_pokemon_p1, 13);

                    sprintf(text1, "| %s --> %s|", oldNameCentered, newNameCentered);
                    center_text(col_left, text1, COL_WIDTH);
                }
            }
            else if (i == 21)
            {
                char text[128];
                sprintf(text, "|   %-12s    %-12s   |", m1_p2, m2_p2);
                center_text(col_right, text, COL_WIDTH);

                center_text(col_left, "----------------------------------", COL_WIDTH);
            }
            else if (i == 22)
            {
                center_text(col_right, "|                                  | ", COL_WIDTH);
            }
            else if (i == 23)
            {
                char text[128];
                sprintf(text, "| 3) %-12s  4) %-12s |", m3_p1, m4_p1);
                center_text(col_right, text, COL_WIDTH);
            }
            else if (i == 24)
            {
                char text[128];
                sprintf(text, "|   %-12s    %-12s   |", m3_p2, m4_p2);
                center_text(col_right, text, COL_WIDTH);
            }
            else if (i == 25)
            {
                center_text(col_right, "----------------------------------", COL_WIDTH);
            }
        }
        else
        {

            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        sprintf(line, "%s|%s\n", col_left, col_right);
        strcat(screen, line);
    }
    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);
    printf("%s", screen);
}
void p2ChangePokemon(Player *pl1, Player *pl2, int action, int actionO)
{

    char screen[4096] = "";
    char line[200];

    char col_left[50];
    char col_right[50];
    char title_buffer[100];

    const char *p1_name = pl1->name;
    const char *p2_name = pl2->name;
    int total_width = (COL_WIDTH * 2) + 1;

    center_text(title_buffer, "POKEMON BATTLE ARENA", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    for (int i = 0; i < COL_HEIGHT; i++)
    {

        col_left[0] = '\0';
        col_right[0] = '\0';

        if (i == 1)
        {
            center_text(col_left, p1_name, COL_WIDTH);
            center_text(col_right, p2_name, COL_WIDTH);
        }
        else if (i == 3)
        {
            char p1PokemonNumbers[50];
            sprintf(p1PokemonNumbers, "Pokemons : 6 / %d", 6 - p1_faintedPokemon);

            char p2PokemonNumbers[50];
            sprintf(p2PokemonNumbers, "Pokemons : 6 / %d", 6 - p2_faintedPokemon);

            center_text(col_left, p1PokemonNumbers, COL_WIDTH);
            center_text(col_right, p2PokemonNumbers, COL_WIDTH);
        }
        else if (i == 8)
        {
            align_left(col_left, current_name_p1_p, COL_WIDTH);
            align_right(col_right, current_name_p2_p, COL_WIDTH);
        }

        else if (i == 9)
        {
            char p1TypeText[50];
            sprintf(p1TypeText, "%s - %s", pokemon_p1_type1, pokemon_p1_type2);
            char p2TypeText[50];
            sprintf(p2TypeText, "%s - %s", pokemon_p2_type1, pokemon_p2_type2);
            align_right(col_right, p2TypeText, COL_WIDTH);
            align_left(col_left, p1TypeText, COL_WIDTH);
        }
        else if (i == 15)
        {
            char textp1[50];
            sprintf(textp1, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p1_p, max_hp_p1_p, speed_p1_p, attack_p1_p, defense_p1_p, sp_attack_p1_p, sp_defense_p1_p);
            char textp2[50];
            sprintf(textp2, "HP:%2d/%-2d Speed:%-2d | Atk:%2d Def:%2d | SpA:%2d SpD:%2d",
                    current_hp_p2_p, max_hp_p2_p, speed_p2_p, attack_p2_p, defense_p2_p, sp_attack_p2_p, sp_defense_p2_p);

            align_right(col_right, textp2, COL_WIDTH);
            align_left(col_left, textp1, COL_WIDTH);
        }
        else if (i == 18)
        {
            center_text(col_right, "Change Pokemon", COL_WIDTH);
            if (action == 1)
                center_text(col_left, " Attack ", COL_WIDTH);
            else if (action == 2)
                center_text(col_left, " Change ", COL_WIDTH);
        }
        else if (i == 19)
        {
            center_text(col_right, "----------------------------------", COL_WIDTH);
            center_text(col_left, "----------------------------------", COL_WIDTH);
        }
        else if (i >= 20 && i < 26)
        {

            if (i == 20)
            {
                char text[128];
                sprintf(text, "| 1) %-12s  2) %-12s |", p2_pokemons1, p2_pokemons2);
                center_text(col_right, text, COL_WIDTH);

                char text1[60];
                if (action == 1)
                {
                    char moveName[50];
                    strcpy(moveName, pl1->pokemons[pl1->currentIndex].moves[actionO - 1].name);
                    char centeredMove[50];
                    center_text(centeredMove, moveName, 32);
                    sprintf(text1, "|%s|", centeredMove);
                    center_text(col_left, text1, COL_WIDTH);
                }
                else if (action == 2)
                {
                    char oldNameCentered[20];
                    char newNameCentered[20];

                    center_text(oldNameCentered, old_pokemon_p1, 13);
                    center_text(newNameCentered, new_pokemon_p1, 13);

                    sprintf(text1, "| %s --> %s|", oldNameCentered, newNameCentered);
                    center_text(col_left, text1, COL_WIDTH);
                }
            }
            else if (i == 21)
            {

                char text[128];

                sprintf(text, "|                                  |");
                center_text(col_right, text, COL_WIDTH);
                center_text(col_left, "----------------------------------", COL_WIDTH);
            }
            else if (i == 22)
            {
                char text[128];
                sprintf(text, "| 3) %-12s  4) %-12s |", p2_pokemons3, p2_pokemons4);
                center_text(col_right, text, COL_WIDTH);
                center_text(col_left, "", COL_WIDTH);
            }
            else if (i == 23)
            {

                char text[128];

                sprintf(text, "|                                  |");
                center_text(col_right, text, COL_WIDTH);
                center_text(col_left, "", COL_WIDTH);
            }
            else if (i == 24)
            {

                char text[128];
                sprintf(text, "| 5) %-12s  6) %-12s |", p2_pokemons5, p2_pokemons6);
                center_text(col_right, text, COL_WIDTH);
                center_text(col_left, "", COL_WIDTH);
            }
            else if (i == 25)
            {
                center_text(col_right, "----------------------------------", COL_WIDTH);
                center_text(col_left, "", COL_WIDTH);
            }
        }
        else
        {

            center_text(col_left, "", COL_WIDTH);
            center_text(col_right, "", COL_WIDTH);
        }

        sprintf(line, "%s|%s\n", col_left, col_right);
        strcat(screen, line);
    }
    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);
    printf("%s", screen);
}

void generalMessage(int action, Player *pl1, Player *pl2, int p1ActionN, int p2ActionN, int realDmg1, int realDmg2)
{
    playerSetup(pl1, pl2);

    char displayLines[15][200];

    for (int k = 0; k < 15; k++)
        strcpy(displayLines[k], "");

    int max_lines = 6;
    int total_width = (COL_WIDTH * 2) + 1;
    char title_buffer[100];
    char screen[4096] = "";
    char line[200];

    char msg1[150];
    char msg2[150];
    char text[150];
    char switchMsg1[150];
    char switchMsg2[150];

    Pokemon *p1_mon = &pl1->pokemons[pl1->currentIndex];
    Pokemon *p2_mon = &pl2->pokemons[pl2->currentIndex];

    center_text(title_buffer, "BATTLE LOG", total_width);
    sprintf(line, "%s\n", title_buffer);
    strcat(screen, line);

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    center_text(displayLines[0], "", total_width);

    switch (action)
    {
    case 1:
        center_text(displayLines[0], "|", total_width);
        if (p1_mon->speed >= p2_mon->speed)
        {
            sprintf(msg1, "%s : %-s -> %d Dmg", p1_mon->name, p1_mon->moves[p1ActionN - 1].name, realDmg1);
            center_text(displayLines[1], msg1, total_width);

            if (p2_mon->currentHP <= 0)
            {
                sprintf(msg2, "%s's pokemon %s fainted!", pl2->name, p2_mon->name);
                center_text(displayLines[2], msg2, total_width);
            }
            else
            {
                sprintf(msg2, "%s : %s -> %d Dmg", p2_mon->name, p2_mon->moves[p2ActionN - 1].name, realDmg2);
                center_text(displayLines[2], msg2, total_width);

                if (p1_mon->currentHP <= 0)
                {
                    sprintf(msg2, "%s's pokemon %s fainted!", pl1->name, p1_mon->name);
                    center_text(displayLines[3], msg2, total_width);
                }
            }
        }
        else
        {
            sprintf(msg1, "%s : %s -> %d Dmg", p2_mon->name, p2_mon->moves[p2ActionN - 1].name, realDmg2);
            center_text(displayLines[1], msg1, total_width);

            if (p1_mon->currentHP <= 0)
            {
                sprintf(msg2, "%s's pokemon %s fainted!", pl1->name, p1_mon->name);
                center_text(displayLines[2], msg2, total_width);
            }
            else
            {
                sprintf(msg2, "%s : %s -> %d Dmg", p1_mon->name, p1_mon->moves[p1ActionN - 1].name, realDmg1);
                center_text(displayLines[2], msg2, total_width);

                if (p2_mon->currentHP <= 0)
                {
                    sprintf(msg2, "%s's pokemon %s fainted!", pl2->name, p2_mon->name);
                    center_text(displayLines[3], msg2, total_width);
                }
            }
        }
        break;

    case 2:
        center_text(displayLines[0], "|", total_width);
        sprintf(text, " %s Switched Pokemon!", pl1->name);
        center_text(displayLines[1], text, total_width);

        char p1Old[20], p1New[20];
        center_text(p1Old, old_pokemon_p1, 12);
        center_text(p1New, new_pokemon_p1, 12);

        sprintf(switchMsg1, " %s --> %s ", p1Old, p1New);
        center_text(displayLines[2], switchMsg1, total_width);

        sprintf(msg1, "%s : %s -> %d Dmg", p2_mon->name, p2_mon->moves[p2ActionN - 1].name, realDmg2);
        center_text(displayLines[3], msg1, total_width);

        if (p1_mon->currentHP <= 0)
        {
            sprintf(msg2, "%s's pokemon %s fainted!", pl1->name, p1_mon->name);
            center_text(displayLines[4], msg2, total_width);
        }
        break;

    case 3:
        center_text(displayLines[0], "|", total_width);
        sprintf(text, " %s Switched Pokemon!", pl2->name);
        center_text(displayLines[1], text, total_width);

        char p2Old[20], p2New[20];
        center_text(p2Old, old_pokemon_p2, 12);
        center_text(p2New, new_pokemon_p2, 12);

        sprintf(switchMsg2, " %s --> %s ", p2Old, p2New);
        center_text(displayLines[2], switchMsg2, total_width);

        sprintf(msg1, "%s : %s -> %d Dmg", p1_mon->name, p1_mon->moves[p1ActionN - 1].name, realDmg1);
        center_text(displayLines[3], msg1, total_width);

        if (p2_mon->currentHP <= 0)
        {
            sprintf(msg2, "%s's pokemon %s fainted!", pl2->name, p2_mon->name);
            center_text(displayLines[4], msg2, total_width);
        }
        break;

    case 4:
        center_text(displayLines[0], "|", total_width);
        center_text(displayLines[1], "Both Players Switched Pokemon!", total_width);

        char old1[20], new1[20];
        center_text(old1, old_pokemon_p1, 12);
        center_text(new1, new_pokemon_p1, 12);
        sprintf(switchMsg1, "%s :  %s --> %s ", pl1->name, old1, new1);
        center_text(displayLines[2], switchMsg1, total_width);

        char old2[20], new2[20];
        center_text(old2, old_pokemon_p2, 12);
        center_text(new2, new_pokemon_p2, 12);
        sprintf(switchMsg2, "%s :  %s --> %s ", pl2->name, old2, new2);
        center_text(displayLines[3], switchMsg2, total_width);
        break;

    case 5:
        center_text(displayLines[0], "Initialize Start", total_width);
        center_text(displayLines[1], "Types are initialized", total_width);
        center_text(displayLines[2], "Moves are initialized", total_width);
        center_text(displayLines[3], "Pokemons are initialized", total_width);
        center_text(displayLines[4], "Players are initialized", total_width);
        center_text(displayLines[5], "Game is starting...", total_width);
        break;

    case 6:
        max_lines = 15;
        center_text(displayLines[0], "Game Finished", total_width);
        center_text(displayLines[1], "The Winner", total_width);
        center_text(displayLines[2], "Is", total_width);
        sprintf(switchMsg1, "%s", pl2->name);
        center_text(displayLines[3], switchMsg1, total_width);
        center_text(displayLines[4], "Congrulations", total_width);

        center_text(displayLines[7], "PPPP   OOO  K  K  EEEE M   M  OOO  N   N   .-.   .-.   ", total_width);
        center_text(displayLines[8], "P   P O   O K K   E    MM MM O   O NN  N  /   \\ /   \\  ", total_width);
        center_text(displayLines[9], "P   P O   O KK    EEE  M M M O   O N N N  \\    V    /  ", total_width);
        center_text(displayLines[10], "PPPP  O   O K K   E    M   M O   O N  NN   \\       /   ", total_width);
        center_text(displayLines[11], "P     O   O K  K  E    M   M O   O N   N    \\     /    ", total_width);
        center_text(displayLines[12], "P      OOO  K   K EEEE M   M  OOO  N   N     \\___/     ", total_width);
        break;

    case 7:
        max_lines = 15;
        center_text(displayLines[0], "Game Finished", total_width);
        center_text(displayLines[1], "The Winner", total_width);
        center_text(displayLines[2], "Is", total_width);
        sprintf(switchMsg1, "%s", pl1->name);
        center_text(displayLines[3], switchMsg1, total_width);
        center_text(displayLines[4], "Congrulations", total_width);
        center_text(displayLines[7], "PPPP   OOO  K  K  EEEE M   M  OOO  N   N   .-.   .-.   ", total_width);
        center_text(displayLines[8], "P   P O   O K K   E    MM MM O   O NN  N  /   \\ /   \\  ", total_width);
        center_text(displayLines[9], "P   P O   O KK    EEE  M M M O   O N N N  \\    V    /  ", total_width);
        center_text(displayLines[10], "PPPP  O   O K K   E    M   M O   O N  NN   \\       /   ", total_width);
        center_text(displayLines[11], "P     O   O K  K  E    M   M O   O N   N    \\     /    ", total_width);
        center_text(displayLines[12], "P      OOO  K   K EEEE M   M  OOO  N   N     \\___/     ", total_width);
        break;

    default:
        break;
    }

    for (int i = 0; i < max_lines; i++)
    {
        if (strlen(displayLines[i]) > 0)
        {
            strcat(screen, displayLines[i]);
            strcat(screen, "\n");
        }
        else
        {
            char emptyRow[200];
            char left[COL_WIDTH + 1] = "", right[COL_WIDTH + 1] = "";

            center_text(left, "", COL_WIDTH);
            center_text(right, "", COL_WIDTH);
            sprintf(emptyRow, "%s|%s\n", left, right);
            strcat(screen, emptyRow);
        }
    }

    memset(line, '-', total_width);
    line[total_width] = '\n';
    line[total_width + 1] = '\0';
    strcat(screen, line);

    printf("%s", screen);
    playerSetup(pl1, pl2);
}

void printInvalid()
{

    int total_width = (COL_WIDTH * 2) + 1;

    char line[200];
    char msg[200];

    memset(line, '-', total_width);
    line[total_width] = '\0';

    center_text(msg, "Enter Valid Number", total_width);

    printf("\n");
    printf("%s\n", line);
    printf("%s\n", msg);
    printf("%s\n", line);
    printf("\n");
}
void printFainted()
{

    int total_width = (COL_WIDTH * 2) + 1;

    char line[200];
    char msg[200];

    memset(line, '-', total_width);
    line[total_width] = '\0';

    center_text(msg, "You can't choose fainted pokemon ", total_width);

    printf("\n");
    printf("%s\n", line);
    printf("%s\n", msg);
    printf("%s\n", line);
    printf("\n");
}
void printSame()
{

    int total_width = (COL_WIDTH * 2) + 1;

    char line[200];
    char msg[200];

    memset(line, '-', total_width);
    line[total_width] = '\0';

    center_text(msg, "You can't choose same pokemon ", total_width);

    printf("\n");
    printf("%s\n", line);
    printf("%s\n", msg);
    printf("%s\n", line);
    printf("\n");
}
