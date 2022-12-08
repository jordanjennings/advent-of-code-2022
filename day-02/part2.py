from __future__ import annotations
from enum import Enum


class Outcome(Enum):
    LOSE = 0
    DRAW = 3
    WIN = 6

    @staticmethod
    def parse(outcome: str) -> Outcome:
        return {
            "X": Outcome.LOSE,
            "Y": Outcome.DRAW,
            "Z": Outcome.WIN
        }[outcome]


class Move(Enum):
    ROCK = 1
    PAPER = 2
    SCISSORS = 3

    @staticmethod
    def parse(move: str) -> Move:
        return {
            "A": Move.ROCK,
            "B": Move.PAPER,
            "C": Move.SCISSORS
        }[move]

    def outcome_vs(self, other: Move):
        if self == other:
            return Outcome.DRAW

        if self == Move.ROCK:
            if other == Move.PAPER:
                return Outcome.LOSE
            if other == Move.SCISSORS:
                return Outcome.WIN

        if self == Move.PAPER:
            if other == Move.ROCK:
                return Outcome.WIN
            if other == Move.SCISSORS:
                return Outcome.LOSE

        if self == Move.SCISSORS:
            if other == Move.ROCK:
                return Outcome.LOSE
            if other == Move.PAPER:
                return Outcome.WIN


def calculate_move(p1_move: Move, desired_outcome: Outcome):
    for move in Move:
        if move.outcome_vs(p1_move) == desired_outcome:
            return move


with open("input.txt", "r") as f:
    p2_score = 0
    for index, line in enumerate(f):
        p1, p2 = line.split()
        p1_move = Move.parse(p1)
        p2_outcome = Outcome.parse(p2)
        p2_move = calculate_move(p1_move, p2_outcome)
        p2_score += p2_outcome.value + p2_move.value

        print(f"p1: {p1} p2: {p2} p2_outcome.value: {p2_outcome.value} p2_move.value: {p2_move.value}")

    print(f"p2_score: {p2_score}")
