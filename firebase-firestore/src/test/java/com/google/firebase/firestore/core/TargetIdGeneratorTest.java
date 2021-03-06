// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/** Tests TargetIdGenerator */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TargetIdGeneratorTest {

  @Test
  public void testConstructor() {
    assertEquals(2, TargetIdGenerator.getLocalStoreIdGenerator(0).nextId());
    assertEquals(1, TargetIdGenerator.getSyncEngineGenerator(0).nextId());
  }

  @Test
  public void testSkipPast() {
    TargetIdGenerator gen = new TargetIdGenerator(1, -1);
    assertEquals(1, gen.nextId());

    gen = new TargetIdGenerator(1, 2);
    assertEquals(3, gen.nextId());

    gen = new TargetIdGenerator(1, 4);
    assertEquals(5, gen.nextId());

    for (int i = 4; i < 12; i++) {
      TargetIdGenerator gen0 = new TargetIdGenerator(0, i);
      TargetIdGenerator gen1 = new TargetIdGenerator(1, i);
      assertEquals(i + 2 & ~1, gen0.nextId());
      assertEquals(i + 1 | 1, gen1.nextId());
    }

    gen = new TargetIdGenerator(1, 12);
    assertEquals(13, gen.nextId());

    gen = new TargetIdGenerator(0, 22);
    assertEquals(24, gen.nextId());
  }

  @Test
  public void testIncrement() {
    TargetIdGenerator gen = new TargetIdGenerator(0, 0);
    assertEquals(2, gen.nextId());
    assertEquals(4, gen.nextId());
    assertEquals(6, gen.nextId());

    gen = new TargetIdGenerator(0, 46);
    assertEquals(48, gen.nextId());
    assertEquals(50, gen.nextId());
    assertEquals(52, gen.nextId());
    assertEquals(54, gen.nextId());

    gen = new TargetIdGenerator(1, 0);
    assertEquals(1, gen.nextId());
    assertEquals(3, gen.nextId());
    assertEquals(5, gen.nextId());

    gen = new TargetIdGenerator(1, 46);
    assertEquals(47, gen.nextId());
    assertEquals(49, gen.nextId());
    assertEquals(51, gen.nextId());
    assertEquals(53, gen.nextId());
  }
}
