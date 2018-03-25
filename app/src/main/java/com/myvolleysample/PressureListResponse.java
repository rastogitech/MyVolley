package com.myvolleysample;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Copyright 2017 Rahul Rastogi. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PressureListResponse extends BaseResponse {

    @SerializedName("Result")
    private List<Pressure> pressureList;

    public List<Pressure> getPressureList() {
        return pressureList;
    }

    public void setPressureList(List<Pressure> pressureList) {
        this.pressureList = pressureList;
    }

}
