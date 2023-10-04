package com.example.kapitanov1;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private Integer mCols, mRows;
    private Resources mRes; // Ресурсы приложения


    private ArrayList<String> arrPict; // массив картинок
    private String PictureCollection; // Префикс набора картинок
    public static enum Status {CELL_OPEN, CELL_CLOSE, CELL_DELETE};
    private ArrayList<Status> arrStatus; // состояние ячеек
    private int closedCards = 0;
    private int numberOfCards = 22;


    // Добавим переменную для отслеживания первой открытой карточки
    private int firstCardPosition = -1;
    private boolean isClickable = true; // флаг для отслеживания кликабельности

    private Handler handler = new Handler();
    private static final long DELAY_MS = 1000; // Задержка в миллисекундах


    public GridAdapter(Context context, int cols, int rows, String pictureCollection) {
        mContext = context;
        mCols = cols;
        mRows = rows;

        arrPict = new ArrayList<String>();

        // Устанавливаем PictureCollection
        PictureCollection = pictureCollection;

        // Получаем все ресурсы приложения
        mRes = mContext.getResources();

        // Метод заполняющий массив vecPict
        makePictArray();
        closeAllCells();

        // Инициализируем массив arrStatus так, чтобы все карточки были закрытыми (CELL_CLOSE)
        arrStatus = new ArrayList<>(Collections.nCopies(mCols * mRows, Status.CELL_CLOSE));
    }

    private void makePictArray() {
        // Очищаем массив
        arrPict.clear();
        Random random = new Random();
        // Добавляем "лицевую" сторону карточек
        while (arrPict.size() < mRows * mCols) {
            // Генерируем случайное число
            int randomNumber = random.nextInt(numberOfCards);
            if (!arrPict.contains(PictureCollection + Integer.toString(randomNumber))) {
                arrPict.add(PictureCollection + Integer.toString(randomNumber));
                arrPict.add(PictureCollection + Integer.toString(randomNumber));
            }
        }
        // Перемешиваем
        Collections.shuffle(arrPict);
    }

    @Override
    public int getCount() {
        return mCols * mRows;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void openCell(int position) {
        arrStatus.set(position, Status.CELL_OPEN);
        notifyDataSetChanged(); // Обновить адаптер, чтобы обновить отображение
    }

    public void closeCell(int position) {
        arrStatus.set(position, Status.CELL_CLOSE);
        notifyDataSetChanged(); // Обновить адаптер, чтобы обновить отображение
    }

    private void closeAllCells() {
        arrStatus = new ArrayList<>();
        for (int i = 0; i < mCols * mRows; i++) {
            arrStatus.add(Status.CELL_CLOSE);
        }
    }

    public void deleteCell(int position) {
        arrStatus.set(position, Status.CELL_DELETE);
        notifyDataSetChanged(); // Обновить адаптер, чтобы обновить отображение
    }

    // Метод для проверки совпадения пары карточек по их позициям
    public boolean isMatchingPair(int position1, int position2) {
        if (position1 >= 0 && position1 < arrPict.size() &&
                position2 >= 0 && position2 < arrPict.size()) {
            String image1 = arrPict.get(position1);
            String image2 = arrPict.get(position2);
            return image1.equals(image2);
        }
        return false; // Возвращаем false, если позиции недопустимы или изображения не совпадают
    }

    public void updatePictureCollection(String newPictureCollection) {
        PictureCollection = newPictureCollection;
        makePictArray();
        closeAllCells();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view; // для вывода картинки

        if (convertView == null)
            view = new ImageView(mContext);
        else
            view = (ImageView) convertView;

        switch (arrStatus.get(position)) {
            case CELL_OPEN:
                // Если карточка открыта, устанавливаем ее реальное изображение
                Integer drawableId = mRes.getIdentifier(arrPict.get(position), "drawable", mContext.getPackageName());
                view.setImageResource(drawableId);
                break;
            case CELL_CLOSE:
                // Если карточка закрыта, устанавливаем изображение задней стороны
                view.setImageResource(R.drawable.card_back);
                break;
            default:
                view.setImageResource(R.drawable.none);
        }
        return view;
    }

    public boolean checkEnd() {
        if (arrStatus.contains(Status.CELL_OPEN) || arrStatus.contains(Status.CELL_CLOSE))
            return false;
        return true;
    }

    public boolean handleCardClick(final int position) {
        boolean end = false;

        // Проверяем, что карточка закрыта (CELL_CLOSE) и можно обрабатывать клик
        if (arrStatus.get(position) == Status.CELL_CLOSE && isClickable) {
            // Если это первая карточка, сохраняем ее позицию
            if (firstCardPosition == -1) {
                firstCardPosition = position;
                openCell(position);
            } else {
                // Если это вторая карточка, сравниваем с первой
                openCell(position);

                if (isMatchingPair(firstCardPosition, position)) {
                    closedCards += 2;

                    // Совпадение, добавляем задержку перед закрытием
                    final int firstPos = firstCardPosition;
                    final int secondPos = position;
                    isClickable = false; // Блокируем обработку кликов

                    if (closedCards == mCols * mRows)
                        end = true;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteCell(firstPos);
                            deleteCell(secondPos);
                            isClickable = true; // Разрешаем обработку кликов после анимации
                        }
                    }, DELAY_MS);
                } else {
                    // Несовпадение, закрываем обе карточки с задержкой
                    final int firstPos = firstCardPosition; // Сохраняем позицию первой карточки
                    isClickable = false; // Блокируем обработку кликов
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeCell(firstPos); // Используем сохраненную позицию
                            closeCell(position);
                            isClickable = true; // Разрешаем обработку кликов после анимации
                        }
                    }, DELAY_MS);
                }
                firstCardPosition = -1; // Сбрасываем первую карточку
            }
        }
        return end;
    }
}