
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>
#include "structs.h"
#include "functions.h"
#include "menu.h"

int dmg1_out = 0;
int dmg2_out = 0;
int moveList[48];
int moveListCount = 48;
int globalUsedMoves[486] = {0};

int moveslist(int moveList[])
{
    int count = 0;
    while (count < 48)
    {
        int random = rand() % 486;
        int sameId = 0;

        for (int i = 0; i < count; i++)
        {
            if (moveList[i] == random)
            {
                sameId = 1;
                break;
            }
        }

        if (sameId == 0)
        {
            moveList[count] = random;
            count++;
        }
    }
    return 0;
}

void assignMovesToPokemon(Pokemon *p, Move move[])
{
    int assignedMoves = 0;
    int failSafeCounter = 0;
    while (assignedMoves < 4)
    {

        if (moveListCount >= 48)
        {
            moveslist(moveList);
            moveListCount = 0;
        }

        int candidateIndex = moveList[moveListCount];
        moveListCount++;

        if (globalUsedMoves[candidateIndex] == 1)
        {

            if (failSafeCounter < 1000)
            {
                failSafeCounter++;
                continue;
            }
        }

        int alreadyHas = 0;
        for (int k = 0; k < assignedMoves; k++)
        {

            if (strcmp(p->moves[k].name, move[candidateIndex].name) == 0)
            {
                alreadyHas = 1;
                break;
            }
        }
        if (alreadyHas == 1)
        {
            continue;
        }

        int isCompatible = 0;

        if (strcmp(move[candidateIndex].type.name, p->types[0].name) == 0)
            isCompatible = 1;
        else if (strcmp(p->types[1].name, "None") != 0 && strcmp(move[candidateIndex].type.name, p->types[1].name) == 0)
            isCompatible = 1;
        else if (strcmp(move[candidateIndex].type.name, "Normal") == 0)
            isCompatible = 1;

        if (isCompatible == 1)
        {
            p->moves[assignedMoves] = move[candidateIndex];

            globalUsedMoves[candidateIndex] = 1;

            assignedMoves++;
            failSafeCounter = 0;
        }
        failSafeCounter++;
    }
}

void initializeTypes(Type types[])
{

    FILE *typeFile = fopen("types.txt", "r");

    if (typeFile == NULL)
    {
        printf("\n This file cannot be opened \n");
        exit(1);
    }

    for (int i = 0; i < 18; i++)
    {

        fscanf(typeFile, "%s", types[i].name);

        for (int j = 0; j < 18; j++)
        {
            strcpy(types[i].effects[j].atkname, types[i].name);

            fscanf(typeFile, "%s %lf",
                   types[i].effects[j].defname,
                   &types[i].effects[j].multiplier);
        }
        strcpy(types[i].effects[18].atkname, types[i].name);
        strcpy(types[i].effects[18].defname, "None");
        types[i].effects[18].multiplier = 1.0;
    }
    fclose(typeFile);

    strcpy(types[18].name, "None");
    for (int k = 0; k < 19; k++)
    {
        strcpy(types[18].effects[k].atkname, "None");

        if (k < 18)
        {
            strcpy(types[18].effects[k].defname, types[k].name);
        }
        else
        {
            strcpy(types[18].effects[k].defname, "None");
        }

        types[18].effects[k].multiplier = 1.0;
    }
}

void initializeMoves(Move move[], Type type[])
{
    FILE *moveFile = fopen("moves.txt", "r");

    if (moveFile == NULL)
    {
        printf("\n This file cannot be opened \n ");
        exit(1);
    }
    else
    {

        char tempTypeName[25];
        char tempCategory[20];

        for (int i = 0; i < 486; i++)
        {

            fscanf(moveFile, "%s %s %s %lf",
                   move[i].name, tempTypeName, tempCategory, &move[i].atkPower);

            if (strcmp(tempCategory, "Physical") == 0)
            {
                move[i].category = PhysicalAttack;
            }
            else
            {
                move[i].category = SpecialAttack;
            }

            for (int j = 0; j < 18; j++)
            {
                if (strcmp(tempTypeName, type[j].name) == 0)
                {
                    move[i].type = type[j];
                    break;
                }
            }
        }

        fclose(moveFile);
    }
}

void initializePokemons(Pokemon pokemons[], Type type[])
{

    FILE *pokemonFile = fopen("pokemon.txt", "r");

    if (pokemonFile == NULL)
    {
        printf("\n This file cannot be opened \n");
        exit(1);
    }

    char tempType1[20];
    char tempType2[20];
    int loadedcount = 0;

    while (fscanf(pokemonFile, "%s %s %s %lf %lf %lf %lf %lf %lf",
                  pokemons[loadedcount].name,
                  tempType1,
                  tempType2,
                  &pokemons[loadedcount].maxHP,
                  &pokemons[loadedcount].attack,
                  &pokemons[loadedcount].defense,
                  &pokemons[loadedcount].spAtk,
                  &pokemons[loadedcount].spDef,
                  &pokemons[loadedcount].speed) != EOF)
    {
        pokemons[loadedcount].currentHP = pokemons[loadedcount].maxHP;

        for (int j = 0; j < 18; j++)
        {
            if (strcmp(tempType1, type[j].name) == 0)
            {
                pokemons[loadedcount].types[0] = type[j];
                break;
            }
        }

        if (strcmp(tempType2, "-") != 0)
        {
            for (int t = 0; t < 18; t++)
            {
                if (strcmp(tempType2, type[t].name) == 0)
                {
                    pokemons[loadedcount].types[1] = type[t];
                    break;
                }
            }
        }
        else
        {
            pokemons[loadedcount].types[1] = type[18];
        }

        loadedcount++;
        if (loadedcount >= 1015)
            break;
    }
    fclose(pokemonFile);
}

void initialize(Type type[], Move move[], Pokemon pokemon[], Player *player1, Player *player2)
{
    srand(time(NULL));
    initializeTypes(type);
    initializeMoves(move, type);
    initializePokemons(pokemon, type);

    strcpy(player1->name, "Nail");
    player1->currentIndex = 0;

    int count1 = 0;
    while (count1 < 6)
    {
        int r = rand() % 1015;
        int exists = 0;
        for (int k = 0; k < count1; k++)
        {
            if (strcmp(player1->pokemons[k].name, pokemon[r].name) == 0)
                exists = 1;
        }
        if (!exists)
        {
            player1->pokemons[count1] = pokemon[r];
            count1++;
        }
    }

    strcpy(player2->name, "Kerem");
    player2->currentIndex = 0;
    int count2 = 0;
    while (count2 < 6)
    {
        int r = rand() % 1015;
        int exists = 0;
        for (int k = 0; k < count2; k++)
        {
            if (strcmp(player2->pokemons[k].name, pokemon[r].name) == 0)
                exists = 1;
        }
        if (!exists)
        {
            player2->pokemons[count2] = pokemon[r];
            count2++;
        }
    }

    for (int i = 0; i < 6; i++)
    {
        assignMovesToPokemon(&player1->pokemons[i], move);
    }

    for (int i = 0; i < 6; i++)
    {
        assignMovesToPokemon(&player2->pokemons[i], move);
    }

    generalMessage(5, player1, player2, 0, 0, 0, 0);
}

int getValidInt(int min, int max)
{
    int value;
    char term;

    if (scanf("%d", &value) != 1)
    {

        while (getchar() != '\n')
            ;
        return -1;
    }

    term = getchar();
    if (term != '\n')
    {

        while (getchar() != '\n')
            ;
        return -1;
    }

    if (value < min || value > max)
    {
        return -1;
    }

    return value;
}

double calculateDamage(Player *attacker, Player *defender, int moveIndex)
{
    Pokemon *atkMon = &attacker->pokemons[attacker->currentIndex];
    Pokemon *defMon = &defender->pokemons[defender->currentIndex];

    double power = atkMon->moves[moveIndex].atkPower;
    double attack = 0;
    double defense = 0;
    double type_ef1 = 1.0;
    double type_ef2 = 1.0;
    double STAB = 1.0;

    if (atkMon->moves[moveIndex].category == SpecialAttack)
    {
        attack = atkMon->spAtk;
        defense = defMon->spDef;
    }
    else
    {
        attack = atkMon->attack;
        defense = defMon->defense;
    }

    for (int i = 0; i < 18; i++)
    {
        if (strcmp(atkMon->moves[moveIndex].type.effects[i].defname, defMon->types[0].name) == 0)
        {
            type_ef1 = atkMon->moves[moveIndex].type.effects[i].multiplier;
        }

        if (strcmp(atkMon->moves[moveIndex].type.effects[i].defname, defMon->types[1].name) == 0)
        {
            type_ef2 = atkMon->moves[moveIndex].type.effects[i].multiplier;
        }
    }

    if (strcmp(atkMon->moves[moveIndex].type.name, atkMon->types[0].name) == 0)
    {
        STAB = 1.5;
    }
    else if (strcmp(atkMon->moves[moveIndex].type.name, atkMon->types[1].name) == 0)
    {
        STAB = 1.5;
    }

    double dmg = round(power * (attack / defense) * type_ef1 * type_ef2 * STAB);
    return dmg;
}

int checkFainted(Player *p)
{
    Pokemon *current = &p->pokemons[p->currentIndex];

    if (current->currentHP <= 0)
    {
        current->currentHP = 0;

        int isDefeated = 1;

        for (int i = 0; i < 6; i++)
        {
            if (p->pokemons[i].currentHP > 0)
            {
                p->currentIndex = i;
                isDefeated = 0;
                break;
            }
        }

        if (isDefeated)
        {
            return 1;
        }
    }
    return 0;
}

void applyDamage(Player *p1, Player *p2, int actionp1, int actionp2, int *dmg1_out, int *dmg2_out)
{
    Pokemon *p1_p = &p1->pokemons[p1->currentIndex];
    Pokemon *p2_p = &p2->pokemons[p2->currentIndex];

    *dmg1_out = 0;
    *dmg2_out = 0;

    if (p1_p->currentHP > 0 && p2_p->currentHP > 0)
    {

        if (p1_p->speed >= p2_p->speed)
        {

            double d1 = calculateDamage(p1, p2, actionp1 - 1);
            p2_p->currentHP -= d1;
            *dmg1_out = (int)d1;

            if (p2_p->currentHP > 0)
            {
                double d2 = calculateDamage(p2, p1, actionp2 - 1);
                p1_p->currentHP -= d2;
                *dmg2_out = (int)d2;
            }
        }

        else
        {

            double d2 = calculateDamage(p2, p1, actionp2 - 1);
            p1_p->currentHP -= d2;
            *dmg2_out = (int)d2;

            if (p1_p->currentHP > 0)
            {
                double d1 = calculateDamage(p1, p2, actionp1 - 1);
                p2_p->currentHP -= d1;
                *dmg1_out = (int)d1;
            }
        }
    }

    if (p1_p->currentHP <= 0)

        p1_p->currentHP = 0;

    if (p2_p->currentHP <= 0)
        p2_p->currentHP = 0;
}

int playerFaintedPokemons(Player *p)
{
    int faintedPokemons = 0;
    for (int i = 0; i < 6; i++)
    {
        if (p->pokemons[i].currentHP == 0)
        {
            faintedPokemons++;
        }
    }

    return faintedPokemons;
}

void game(Player *p1, Player *p2)
{
    playerSetup(p1, p2);
    int i = 0;
    int gamefinishp1 = 0;
    int gamefinishp2 = 0;
    int p1_action = 0;
    int p1_actionO = 0;

    int p2_action = 0;
    int p2_actionO = 0;

    while (1)
    {
        if (i % 2 == 0)
        {
            while (1)
            {
                p1MainMenu(p1, p2);
                printf("Choose your action : ");

                p1_action = getValidInt(1, 2);

                if (p1_faintedPokemon == 5 && p1_action == 2)
                {
                    printInvalid();
                    continue;
                }

                if (p1_action == 1)
                {
                    while (1)
                    {
                        p1AttackChoose(p1, p2);
                        printf("Choose your attack : ");

                        p1_actionO = getValidInt(1, 4);

                        if (p1_actionO < 5 && p1_actionO > 0)
                        {
                            break;
                        }
                        else
                        {
                            printInvalid();
                        }
                    }
                    break;
                }
                else if (p1_action == 2)
                {
                    while (1)
                    {
                        p1ChangePokemon(p1, p2);
                        printf("Choose your pokemon : ");
                        p1_actionO = getValidInt(1, 6);

                        if (p1_actionO < 7 && p1_actionO > 0)
                        {
                            if ((p1->pokemons[p1_actionO - 1].currentHP) == 0)
                            {
                                printFainted();
                                continue;
                            }
                            else if (p1->currentIndex == p1_actionO - 1)
                            {
                                printSame();
                                continue;
                            }
                            break;
                        }
                        else
                        {
                            printInvalid();
                        }
                    }

                    strcpy(old_pokemon_p1, p1->pokemons[p1->currentIndex].name);
                    strcpy(new_pokemon_p1, p1->pokemons[p1_actionO - 1].name);
                    break;
                }
                else
                {
                    printInvalid();
                }
            }
        }
        else
        {
            while (1)
            {
                p1ActionToP2MainMenu(p1_action, p1, p2, p1_actionO);
                printf("Choose your action : ");

                p2_action = getValidInt(1, 2);

                if (p2_faintedPokemon == 5 && p2_action == 2)
                {
                    printInvalid();
                    continue;
                }

                if (p2_action == 1)
                {
                    while (1)
                    {
                        p2AttackChoose(p1, p2, p1_action, p1_actionO);
                        printf("Choose your attack : ");

                        p2_actionO = getValidInt(1, 4);

                        if ((p1_action == 2 && (p2_actionO < 5 && p2_actionO > 0)))
                        {
                            p1->currentIndex = p1_actionO - 1;

                            double dmg = calculateDamage(p2, p1, p2_actionO - 1);
                            p1->pokemons[p1->currentIndex].currentHP -= dmg;
                            if (p1->pokemons[p1->currentIndex].currentHP < 0)
                                p1->pokemons[p1->currentIndex].currentHP = 0;

                            generalMessage(2, p1, p2, p1_actionO, p2_actionO, 0, (int)dmg);
                            break;
                        }
                        else if ((p1_action == 1 && (p2_actionO < 5 && p2_actionO > 0)))
                        {
                            applyDamage(p1, p2, p1_actionO, p2_actionO, &dmg1_out, &dmg2_out);
                            generalMessage(1, p1, p2, p1_actionO, p2_actionO, dmg1_out, dmg2_out);
                            break;
                        }
                        else
                        {
                            printInvalid();
                            continue;
                        }
                    }

                    gamefinishp1 = checkFainted(p1);
                    gamefinishp2 = checkFainted(p2);
                    break;
                }
                else if (p2_action == 2)
                {
                    while (1)
                    {
                        p2ChangePokemon(p1, p2, p1_action, p1_actionO);
                        printf("Choose your pokemon : ");
                        
                        p2_actionO = getValidInt(1, 6);

                        if (p2_actionO < 7 && p2_actionO > 0)
                        {
                            if ((p2->pokemons[p2_actionO - 1].currentHP) == 0)
                            {
                                printFainted();
                                continue;
                            }
                            else if (p2->currentIndex == p2_actionO - 1)
                            {
                                printSame();
                                continue;
                            }
                            break;
                        }
                        else
                        {
                            printInvalid();
                        }
                    }

                    strcpy(old_pokemon_p2, p2->pokemons[p2->currentIndex].name);
                    strcpy(new_pokemon_p2, p2->pokemons[p2_actionO - 1].name);

                    if (p1_action == 2)
                    {
                        p1->currentIndex = p1_actionO - 1;
                        p2->currentIndex = p2_actionO - 1;
                        generalMessage(4, p1, p2, p1_actionO, p2_actionO, 0, 0);
                    }
                    else if (p1_action == 1)
                    {
                        p2->currentIndex = p2_actionO - 1;

                        double dmg = calculateDamage(p1, p2, p1_actionO - 1);
                        p2->pokemons[p2->currentIndex].currentHP -= dmg;
                        if (p2->pokemons[p2->currentIndex].currentHP < 0)
                            p2->pokemons[p2->currentIndex].currentHP = 0;

                        generalMessage(3, p1, p2, p1_actionO, p2_actionO, (int)dmg, 0);
                    }
                    else
                    {
                        continue;
                    }

                    gamefinishp1 = checkFainted(p1);
                    gamefinishp2 = checkFainted(p2);
                    break;
                }
                else
                {
                    printInvalid();
                }
            }

            p1_action = 0;
            p1_actionO = 0;

            p2_action = 0;
            p2_actionO = 0;
        }

        i++;

        if (gamefinishp1 == 1)
        {
            generalMessage(6, p1, p2, p1_actionO, p2_actionO, dmg1_out, dmg2_out);
            break;
        }
        else if (gamefinishp2 == 1)
        {
            generalMessage(7, p1, p2, p1_actionO, p2_actionO, dmg1_out, dmg2_out);
            break;
        }
        playerSetup(p1, p2);
    }
}
