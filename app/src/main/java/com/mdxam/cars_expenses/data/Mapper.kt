package com.mdxam.cars_expenses.data

import com.mdxam.cars_expenses.domain.CarLog

fun CarLog.entityToDb(): CarLogDb{
    return CarLogDb(id,title,amount,description,timestamp)
}

fun CarLogDb.dbToEntity(): CarLog{
    return CarLog(id,title,amount,description,timestamp)
}