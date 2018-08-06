/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/Apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.tencent.angel.ml.regression.linear

import com.tencent.angel.ml.feature.LabeledData
import com.tencent.angel.ml.task.PredictTask
import com.tencent.angel.utils.HdfsUtil
import com.tencent.angel.worker.task.TaskContext
import org.apache.hadoop.io.{LongWritable, Text}

/**
  * Predict task of linear regression, first pull LinearReg weight vector from PS, second predict the
  * value of local dataset
  *
  * @param ctx : context of current task
  */

class LinearRegPredictTask(val ctx: TaskContext) extends PredictTask[LongWritable, Text](ctx) {

  @throws[Exception]
  def predict(ctx: TaskContext) {
    val lrModel = new LinearRegModel(conf, ctx)
    lrModel.weight.getRow(0);
    val predictResult = lrModel.predict(taskDataBlock)
    System.out.println("predict storage.len=" + predictResult.size)

    HdfsUtil.writeStorage(predictResult, ctx)
  }

  def parse(key: LongWritable, value: Text): LabeledData = {
    dataParser.parse(value.toString)
  }
}