from tkinter import Frame
from abc import ABC, abstractmethod


class AbstractDrawable(ABC):

    @abstractmethod
    def draw(self, frame: Frame):
        pass
