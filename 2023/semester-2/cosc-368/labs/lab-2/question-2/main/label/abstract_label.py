from abc import abstractmethod
from ..abstract_drawable import AbstractDrawable


class AbstractLabel(AbstractDrawable):

    @abstractmethod
    def set_text(self, text: str):
        pass
