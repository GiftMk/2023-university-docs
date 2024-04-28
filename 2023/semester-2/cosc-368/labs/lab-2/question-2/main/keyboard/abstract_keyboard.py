from abc import abstractmethod
from ..abstract_drawable import AbstractDrawable


class AbstractKeyboard(AbstractDrawable):

    @abstractmethod
    def set_on_click(self, on_click):
        pass

    @abstractmethod
    def update_board(self):
        pass
