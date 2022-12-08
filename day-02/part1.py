from __future__ import annotations
from enum import Enum


LOSE = 0
DRAW = 3
WIN = 6


class Move(Enum):
    ROCK = 1
    PAPER = 2
    SCISSORS = 3

    @staticmethod
    def parse(move: str) -> Move:
        if move == "A" or move == "X":
            return Move.ROCK
        if move == "B" or move == "Y":
            return Move.PAPER
        if move == "C" or move == "Z":
            return Move.SCISSORS

    def scoreVs(self, other: Move):
        if self == other:
            return DRAW

        if self == Move.ROCK:
            if other == Move.PAPER:
                return LOSE
            if other == Move.SCISSORS:
                return WIN

        if self == Move.PAPER:
            if other == Move.ROCK:
                return WIN
            if other == Move.SCISSORS:
                return LOSE

        if self == Move.SCISSORS:
            if other == Move.ROCK:
                return LOSE
            if other == Move.PAPER:
                return WIN


with open("input.txt", "r") as f:
    p2_score = 0
    for index, line in enumerate(f):
        p1, p2 = line.split()
        p1_move = Move.parse(p1)
        p2_move = Move.parse(p2)
        p2_outcome_score = p2_move.scoreVs(p1_move)
        p2_score += p2_outcome_score + p2_move.value

        print(f"p1: {p1} p2: {p2} p2_turn_score: {p2_outcome_score} p2_move.value: {p2_move.value}")

    print(f"p2_score: {p2_score}")
