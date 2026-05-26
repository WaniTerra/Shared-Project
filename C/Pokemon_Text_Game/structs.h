#ifndef STRUCTS_H
#define STRUCTS_H
typedef enum AttackCategory
{
   SpecialAttack,
   PhysicalAttack
} AttackCategory;

typedef struct TypeEffects
{
   char atkname[20];
   char defname[20];
   double multiplier;
}TypeEffects;

typedef struct Type
{
   char name[20];
   TypeEffects effects[19];
}Type;

typedef struct Move
{
   char name[30];
   Type type;
   AttackCategory category;
   double atkPower;
}Move;

typedef struct Pokemon
{
   char name[15];
   Type types[2];
   double maxHP;
   double currentHP;
   double attack;
   double defense;
   double spAtk;
   double spDef;
   double speed;
    Move moves[4];
}Pokemon;

typedef struct Player
{
   char name[50];
   struct Pokemon pokemons[6];
   int currentIndex;
}Player;
#endif
