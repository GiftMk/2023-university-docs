from tkinter import *
from abstract_label import AbstractLabel


class TargetLetterLabel(AbstractLabel):

    def __init__(self):
        self.target_letter = StringVar()
        self.padding_y = 20

    def draw(self, frame):
        label = Label(frame, textvariable=self.target_letter)
        label.grid(row=0, column=0, pady=self.padding_y)

    def set_text(self, char):
        self.target_letter.set(char)