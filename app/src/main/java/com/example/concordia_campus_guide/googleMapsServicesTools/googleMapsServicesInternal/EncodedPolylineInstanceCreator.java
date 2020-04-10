/*
 * Copyright 2017 by https://github.com/ArielY15
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesInternal;

import com.google.gson.InstanceCreator;
import com.google.maps.model.EncodedPolyline;

import java.lang.reflect.Type;

public class EncodedPolylineInstanceCreator implements InstanceCreator<EncodedPolyline> {
  private String points;

  public EncodedPolylineInstanceCreator(String points) {
    this.points = points;
  }

  @Override
  public EncodedPolyline createInstance(Type type) {
    return new EncodedPolyline(points);
  }
}