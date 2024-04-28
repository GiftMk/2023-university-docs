from string import ascii_lowercase
from random import randrange, shuffle
from abstract_controller import AbstractController


class TargetLetterController(AbstractController):

    def __init__(self, num_blocks, block_size):
        self.block_size = block_size
        self.num_blocks = num_blocks
        self.current_block = 1
        self.target_letters = self.__get_target_letters()
        self.block = self.__get_next_block()
        self.target_letter = self.update_text()

    def get_text(self):
        return self.target_letter

    def update_text(self):
        if len(self.block) == 0 and self.current_block == self.num_blocks:
            return None

        i = randrange(len(self.block))
        target_letter = self.block.pop(i)

        if len(self.block) == 0 and self.current_block < self.num_blocks:
            self.current_block += 1
            self.block = self.__get_next_block()

        self.target_letter = target_letter
        return target_letter

    def get_current_block(self) -> int:
        return self.current_block

    def __get_next_block(self) -> list:
        block = list(self.target_letters)
        shuffle(block)
        return block

    def __get_target_letters(self) -> list:
        alphabet = list(ascii_lowercase)
        shuffle(alphabet)
        return alphabet[:self.block_size]
